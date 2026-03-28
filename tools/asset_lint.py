#!/usr/bin/env python
from __future__ import annotations

from pathlib import Path
from typing import Any, Dict, Iterable, List, Set, Optional
import argparse

from .common import repo_root, read_csv_rows, setup_logging, read_json

ROOT = repo_root()
CSV_FILE = ROOT / "tools" / "block_definitions.csv"

COMMON_ASSET_ROOT = (
    ROOT / "common" / "src" / "main" / "resources" / "assets" / "additional_lights"
)
GENERATED_ASSET_ROOT = (
    ROOT
    / "build"
    / "generated"
    / "datagen"
    / "resources"
    / "assets"
    / "additional_lights"
)

BLOCKSTATE_DIRS = [
    COMMON_ASSET_ROOT / "blockstates",
    GENERATED_ASSET_ROOT / "blockstates",
]

BLOCK_MODEL_DIRS = [
    COMMON_ASSET_ROOT / "models" / "block",
    GENERATED_ASSET_ROOT / "models" / "block",
]

ASSET_ROOTS = [
    COMMON_ASSET_ROOT,
    GENERATED_ASSET_ROOT,
]


def check_dirs_exist(dirs: Iterable[Path]) -> List[Path]:
    return [d for d in dirs if not d.exists()]


def collect_json_files(dirs: Iterable[Path]) -> List[Path]:
    files: List[Path] = []
    for d in dirs:
        if d.exists():
            files.extend(d.rglob("*.json"))
    return files


def _gather_refs(data: Any) -> List[tuple[str, str]]:
    refs: List[tuple[str, str]] = []
    if isinstance(data, dict):
        for k, v in data.items():
            if k in {"model", "parent"} and isinstance(v, str):
                refs.append((v, "model"))
            elif k == "textures" and isinstance(v, dict):
                for tex in v.values():
                    if isinstance(tex, str) and not tex.startswith("#"):
                        refs.append((tex, "texture"))
            else:
                refs.extend(_gather_refs(v))
    elif isinstance(data, list):
        for item in data:
            refs.extend(_gather_refs(item))
    return refs


def _resolve(ref: str, kind: str, roots: Iterable[Path]) -> Path | None:
    if ref.startswith("#"):
        return None
    if ":" in ref:
        ns, path = ref.split(":", 1)
    else:
        ns, path = "minecraft", ref
    if ns != "additional_lights":
        return None
    if kind == "texture":
        sub = Path("textures") / f"{path}.png"
    else:
        if path.startswith("block/"):
            sub = Path("models") / path
            sub = sub.with_suffix(".json")
        elif path.startswith("item/"):
            sub = Path("models") / path
            sub = sub.with_suffix(".json")
        else:
            sub = Path("models") / f"{path}.json"
    for root in roots:
        candidate = root / sub
        if candidate.exists():
            return candidate
    return roots[0] / sub


def check_references(json_files: List[Path], roots: List[Path]) -> Dict[str, Set[str]]:
    missing: Dict[str, Set[str]] = {}
    for f in json_files:
        data = read_json(f)
        refs = _gather_refs(data)
        for ref, kind in refs:
            resolved = _resolve(ref, kind, roots)
            if resolved is not None and not resolved.exists():
                missing.setdefault(str(f), set()).add(ref)
    return missing


def collect_reg_names(csv_path: Path) -> List[str]:
    return [reg for _, reg, _ in read_csv_rows(csv_path)]


def _collect_json_file_index(dirs: Iterable[Path], *, recursive: bool) -> Dict[str, Set[Path]]:
    index: Dict[str, Set[Path]] = {}
    for d in dirs:
        if not d.exists():
            continue
        iterator = d.rglob("*.json") if recursive else d.glob("*.json")
        for p in iterator:
            index.setdefault(p.name, set()).add(p)
    return index


def check_flat_json_assets(reg_names: List[str], dirs: List[Path]) -> Dict[str, Any]:
    expected = {f"{name}.json" for name in reg_names}
    index = _collect_json_file_index(dirs, recursive=False)
    actual = set(index.keys())
    duplicates = {name: paths for name, paths in index.items() if len(paths) > 1}
    return {
        "missing": expected - actual,
        "extra": actual - expected,
        "duplicates": duplicates,
    }


def check_recursive_json_presence(reg_names: List[str], dirs: List[Path]) -> Dict[str, Any]:
    expected = {f"{name}.json" for name in reg_names}
    index = _collect_json_file_index(dirs, recursive=True)
    actual = set(index.keys())
    duplicates = {name: paths for name, paths in index.items() if name in expected and len(paths) > 1}
    return {"missing": expected - actual, "duplicates": duplicates}


def parse_args(argv: Optional[List[str]] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Validate asset files")
    parser.add_argument(
        "--check-only",
        action="store_true",
        help="Run validation only and exit non-zero on errors",
    )
    return parser.parse_args(argv)


def main(*, check_only: bool = False) -> None:
    logger = setup_logging()

    scan_dirs = [*BLOCKSTATE_DIRS, *BLOCK_MODEL_DIRS]
    reg_names = collect_reg_names(CSV_FILE)

    blockstate_diff = check_flat_json_assets(reg_names, BLOCKSTATE_DIRS)
    if blockstate_diff["missing"]:
        logger.error("missing blockstates: %s", ", ".join(sorted(blockstate_diff["missing"])))
        raise SystemExit(1)
    if blockstate_diff["extra"]:
        logger.error("extra blockstates: %s", ", ".join(sorted(blockstate_diff["extra"])))
        raise SystemExit(1)
    if blockstate_diff["duplicates"]:
        for name, paths in sorted(blockstate_diff["duplicates"].items()):
            logger.error("%s: duplicated blockstate json: %s", name, ", ".join(sorted(map(str, paths))))
        raise SystemExit(1)

    model_diff = check_recursive_json_presence(reg_names, BLOCK_MODEL_DIRS)
    if model_diff["missing"]:
        logger.error("missing block models: %s", ", ".join(sorted(model_diff["missing"])))
        raise SystemExit(1)
    if model_diff["duplicates"]:
        for name, paths in sorted(model_diff["duplicates"].items()):
            logger.error("%s: duplicated block model json: %s", name, ", ".join(sorted(map(str, paths))))
        raise SystemExit(1)

    json_files = collect_json_files(scan_dirs)
    ref_errors = check_references(json_files, ASSET_ROOTS)
    if ref_errors:
        for file, refs in ref_errors.items():
            logger.error("%s: missing references: %s", file, ", ".join(sorted(refs)))
        raise SystemExit(1)

    logger.info("Asset files check passed.")


if __name__ == "__main__":
    ns = parse_args()
    main(check_only=ns.check_only)
