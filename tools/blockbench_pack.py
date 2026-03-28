#!/usr/bin/env python





from __future__ import annotations

import argparse
import json
import os
import shutil
import zipfile
from pathlib import Path
from typing import Iterable, Optional

from .common import repo_root, setup_logging


ROOT = repo_root()

COMMON_ASSET_ROOT = (
    ROOT / "common" / "src" / "main" / "resources" / "assets" / "additional_lights"
)
GENERATED_MODEL_ROOT = (
    ROOT
    / "build"
    / "generated"
    / "datagen"
    / "resources"
    / "assets"
    / "additional_lights"
    / "models"
)


def _default_pack_root() -> Path:
    return ROOT / "run" / "blockbench_pack"


def _ensure_dir(path: Path) -> None:
    path.mkdir(parents=True, exist_ok=True)


def _iter_files(root: Path) -> Iterable[Path]:
    yield from (p for p in root.rglob("*") if p.is_file())


def copy_tree(*, src: Path, dst: Path, overwrite: bool) -> int:
    if not src.exists():
        return 0

    copied = 0
    for file_path in _iter_files(src):
        rel = file_path.relative_to(src)
        out_path = dst / rel
        _ensure_dir(out_path.parent)
        if out_path.exists() and not overwrite:
            continue
        shutil.copy2(file_path, out_path)
        copied += 1
    return copied


def _load_minecraft_version() -> str:
    import tomllib

    toml_path = ROOT / "gradle" / "libs.versions.toml"
    data = tomllib.loads(toml_path.read_text(encoding="utf-8"))
    return str(data["versions"]["minecraft"])


def _default_gradle_user_home() -> Path:
    env = os.environ.get("GRADLE_USER_HOME")
    if env:
        return Path(env).expanduser()
    return Path.home() / ".gradle"


def _default_minecraft_dir() -> Path | None:
    appdata = os.environ.get("APPDATA")
    if appdata:
        candidate = Path(appdata) / ".minecraft"
        if candidate.exists():
            return candidate
    candidate = Path.home() / "AppData" / "Roaming" / ".minecraft"
    if candidate.exists():
        return candidate
    return None


def _jar_has_vanilla_assets(jar: Path) -> bool:
    if not jar.exists() or not jar.is_file():
        return False
    try:
        with zipfile.ZipFile(jar) as zf:
            return any(
                name.startswith("assets/minecraft/textures/")
                or name.startswith("assets/minecraft/models/")
                for name in zf.namelist()
            )
    except zipfile.BadZipFile:
        return False


def _resolve_from_fabric_loom_cache(*, gradle_home: Path, version: str) -> Path | None:
    version_dir = gradle_home / "caches" / "fabric-loom" / version
    candidates = [
        version_dir / "minecraft-client.jar",
        version_dir / "minecraft-client-only.jar",
        version_dir / "minecraft-merged.jar",
    ]
    for jar in candidates:
        if _jar_has_vanilla_assets(jar):
            return jar
    return None


def _resolve_from_gradle_modules_cache(*, gradle_home: Path, version: str) -> Path | None:
    base = (
        gradle_home
        / "caches"
        / "modules-2"
        / "files-2.1"
        / "com.mojang"
        / "minecraft"
        / version
    )
    if not base.exists():
        return None

    for jar in base.rglob("*.jar"):
        if _jar_has_vanilla_assets(jar):
            return jar
    return None


def resolve_minecraft_jar(
    *, minecraft_dir: Path | None, minecraft_version: str | None, minecraft_jar: Path | None
) -> Path:
    if minecraft_jar is not None:
        jar = minecraft_jar
        if not jar.exists():
            raise FileNotFoundError(f"Minecraft jar not found: {jar}")
        return jar

    version = minecraft_version or _load_minecraft_version()

    base = minecraft_dir or _default_minecraft_dir()
    if base is not None:
        jar = base / "versions" / version / f"{version}.jar"
        if _jar_has_vanilla_assets(jar):
            return jar

    gradle_home = _default_gradle_user_home()
    jar = _resolve_from_fabric_loom_cache(gradle_home=gradle_home, version=version)
    if jar is not None:
        return jar
    jar = _resolve_from_gradle_modules_cache(gradle_home=gradle_home, version=version)
    if jar is not None:
        return jar

    launcher_hint = (
        f"{base / 'versions' / version / (version + '.jar')}" if base is not None else "<unknown>"
    )
    raise FileNotFoundError(
        "Minecraft jar not found.\n"
        f"- Tried launcher path: {launcher_hint}\n"
        f"- Tried Gradle Loom cache: {gradle_home / 'caches' / 'fabric-loom' / version}\n"
        f"- Tried Gradle module cache: {gradle_home / 'caches' / 'modules-2' / 'files-2.1' / 'com.mojang' / 'minecraft' / version}\n"
        "Install the target Minecraft version in the launcher, "
        "or pass --minecraft-jar to point to a jar file."
    )


def extract_vanilla_assets(*, pack_root: Path, jar_path: Path, force: bool) -> int:
    assets_root = pack_root / "assets" / "minecraft"
    already = (assets_root / "textures").exists() and (assets_root / "models").exists()
    if already and not force:
        return 0

    extracted = 0
    with zipfile.ZipFile(jar_path) as zf:
        members = [
            name
            for name in zf.namelist()
            if (
                name.startswith("assets/minecraft/textures/")
                or name.startswith("assets/minecraft/models/")
            )
            and not name.endswith("/")
        ]
        _ensure_dir(pack_root)
        for name in members:
            zf.extract(member=name, path=pack_root)
            extracted += 1
    return extracted


def _read_json(path: Path) -> dict:
    with path.open("r", encoding="utf-8") as handle:
        return json.load(handle)


def _write_json(path: Path, data: dict) -> None:
    path.write_text(
        json.dumps(data, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )


def _resolve_model_path(*, pack_root: Path, ref: str) -> Path | None:
    if ":" in ref:
        namespace, model_path = ref.split(":", 1)
    else:
        namespace, model_path = "minecraft", ref

    candidate = pack_root / "assets" / namespace / "models" / f"{model_path}.json"
    return candidate if candidate.exists() else None


def _load_model_chain(*, pack_root: Path, start: Path, max_depth: int = 32) -> list[dict]:
    chain: list[dict] = []
    visited: set[Path] = set()
    current = start
    for _ in range(max_depth):
        if current in visited:
            break
        visited.add(current)
        try:
            data = _read_json(current)
        except json.JSONDecodeError:
            break
        chain.append(data)

        parent = data.get("parent")
        if not isinstance(parent, str) or not parent:
            break
        parent_path = _resolve_model_path(pack_root=pack_root, ref=parent)
        if parent_path is None:
            break
        current = parent_path
    return chain


def _expand_model_for_blockbench(*, chain: list[dict]) -> dict | None:
    elements: list | None = None
    for data in chain:
        candidate = data.get("elements")
        if isinstance(candidate, list):
            elements = candidate
            break
    if elements is None:
        return None

    textures: dict[str, str] = {}
    merged: dict[str, object] = {}
    for data in reversed(chain):
        if isinstance(data.get("textures"), dict):
            textures.update({k: v for k, v in data["textures"].items() if isinstance(v, str)})
        for key in ("ambientocclusion", "gui_light", "render_type", "display"):
            if key in data:
                merged[key] = data[key]

    def _resolve_texture_ref(value: str) -> str:
        current = value
        visited: set[str] = set()
        for _ in range(32):
            if not current.startswith("#"):
                return current
            ref = current[1:]
            if not ref or ref in visited:
                return current
            visited.add(ref)
            next_value = textures.get(ref)
            if not isinstance(next_value, str):
                return current
            current = next_value
        return current

    if textures:
        textures = {k: _resolve_texture_ref(v) for k, v in textures.items()}

    def _remap_texture_key(*, from_key: str, to_key: str) -> None:
        if from_key not in textures:
            return
        if to_key in textures:
            return
        textures[to_key] = textures.pop(from_key)
        for element in elements:
            if not isinstance(element, dict):
                continue
            faces = element.get("faces")
            if not isinstance(faces, dict):
                continue
            for face in faces.values():
                if not isinstance(face, dict):
                    continue
                if face.get("texture") == f"#{from_key}":
                    face["texture"] = f"#{to_key}"

    _remap_texture_key(from_key="texture", to_key="bb_texture")

    out: dict[str, object] = dict(merged)
    if textures:
        out["textures"] = textures
    out["elements"] = elements
    return out


def expand_block_models(*, pack_root: Path, model_root: Path) -> int:
    if not model_root.exists():
        return 0

    expanded = 0
    for model_file in model_root.rglob("*.json"):
        try:
            data = _read_json(model_file)
        except json.JSONDecodeError:
            continue
        if isinstance(data.get("elements"), list):
            continue
        if not isinstance(data.get("parent"), str):
            continue
        chain = _load_model_chain(pack_root=pack_root, start=model_file)
        expanded_model = _expand_model_for_blockbench(chain=chain)
        if expanded_model is None:
            continue
        _write_json(model_file, expanded_model)
        expanded += 1
    return expanded


def expand_generated_block_models(*, pack_root: Path, source_root: Path, dest_root: Path) -> int:

    if not source_root.exists() or not dest_root.exists():
        return 0

    expanded = 0
    for src_file in source_root.rglob("*.json"):
        rel = src_file.relative_to(source_root)
        dst_file = dest_root / rel
        if not dst_file.exists():
            continue
        try:
            data = _read_json(dst_file)
        except json.JSONDecodeError:
            continue
        if isinstance(data.get("elements"), list):
            continue
        if not isinstance(data.get("parent"), str):
            continue
        chain = _load_model_chain(pack_root=pack_root, start=dst_file)
        expanded_model = _expand_model_for_blockbench(chain=chain)
        if expanded_model is None:
            continue
        _write_json(dst_file, expanded_model)
        expanded += 1
    return expanded


def prepare_pack(
    *,
    pack_root: Path,
    overwrite_mod: bool,
    overwrite_generated: bool,
    minecraft_dir: Path | None,
    minecraft_version: str | None,
    minecraft_jar: Path | None,
    force_vanilla: bool,
    include_generated: bool,
) -> None:
    logger = setup_logging()

    pack_assets = pack_root / "assets"
    _ensure_dir(pack_assets)

    mod_dst = pack_assets / "additional_lights"
    copied_mod = copy_tree(src=COMMON_ASSET_ROOT, dst=mod_dst, overwrite=overwrite_mod)

    copied_generated = 0
    if include_generated and GENERATED_MODEL_ROOT.exists():
        gen_src = GENERATED_MODEL_ROOT
        gen_dst = mod_dst / "models"
        copied_generated = copy_tree(src=gen_src, dst=gen_dst, overwrite=overwrite_generated)

    jar_path = resolve_minecraft_jar(
        minecraft_dir=minecraft_dir,
        minecraft_version=minecraft_version,
        minecraft_jar=minecraft_jar,
    )
    extracted = extract_vanilla_assets(pack_root=pack_root, jar_path=jar_path, force=force_vanilla)

    expanded_generated = 0
    if include_generated and (mod_dst / "models" / "block").exists():
        expanded_generated = expand_generated_block_models(
            pack_root=pack_root,
            source_root=GENERATED_MODEL_ROOT / "block",
            dest_root=mod_dst / "models" / "block",
        )

    logger.info("Blockbench pack: %s", pack_root)
    if copied_mod:
        logger.info("Copied Additional Lights assets: %d files", copied_mod)
    else:
        logger.info("Additional Lights assets: no changes (use --overwrite-mod to refresh)")
    if include_generated:
        if GENERATED_MODEL_ROOT.exists():
            logger.info("Copied generated models for preview: %d files", copied_generated)
            if expanded_generated:
                logger.info("Expanded models for Blockbench preview: %d files", expanded_generated)
        else:
            logger.warning("Generated models not found. Run ./gradlew generateBlockData first.")
    if extracted:
        logger.info("Extracted vanilla assets: %d files", extracted)
    else:
        logger.info("Vanilla assets: already present (use --force-vanilla to re-extract)")


def _collect_push_files(pack_mod_root: Path) -> list[Path]:
    files: set[Path] = set()

    block_models = pack_mod_root / "models" / "block"
    if block_models.exists():
        files.update(block_models.rglob("template_*.json"))

    fire_models = block_models / "fire"
    if fire_models.exists():
        files.update(fire_models.rglob("*.json"))

    item_models = pack_mod_root / "models" / "item"
    if item_models.exists():
        files.update(item_models.rglob("*.json"))

    textures = pack_mod_root / "textures"
    if textures.exists():
        files.update(textures.rglob("*"))

    items = pack_mod_root / "items"
    if items.exists():
        files.update(items.rglob("*.json"))

    return sorted(p for p in files if p.is_file())


def push_pack(*, pack_root: Path, overwrite: bool) -> None:
    logger = setup_logging()

    pack_mod_root = pack_root / "assets" / "additional_lights"
    if not pack_mod_root.exists():
        raise FileNotFoundError(
            f"Blockbench pack not found: {pack_mod_root}\n"
            "Run prepare first: ./gradlew prepareBlockbenchPack"
        )

    dst_root = COMMON_ASSET_ROOT
    files = _collect_push_files(pack_mod_root)
    if not files:
        logger.warning("No editable files found under: %s", pack_mod_root)
        return

    copied = 0
    for src_file in files:
        rel = src_file.relative_to(pack_mod_root)
        dst_file = dst_root / rel
        _ensure_dir(dst_file.parent)
        if dst_file.exists() and not overwrite:
            continue
        shutil.copy2(src_file, dst_file)
        copied += 1

    logger.info("Pushed %d files to: %s", copied, dst_root)
    if not overwrite:
        logger.info("Skipped existing files (use --overwrite to force)")


def _build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Prepare/sync Blockbench pack for Additional Lights")
    sub = parser.add_subparsers(dest="command", required=True)

    p_prepare = sub.add_parser("prepare", help="Create/update run/blockbench_pack")
    p_prepare.add_argument(
        "--pack-dir",
        type=Path,
        default=_default_pack_root(),
        help="Blockbench pack directory (default: run/blockbench_pack)",
    )
    p_prepare.add_argument(
        "--overwrite-mod",
        action="store_true",
        help="Overwrite existing Additional Lights files in the pack",
    )
    p_prepare.add_argument(
        "--no-generated",
        action="store_true",
        help="Do not copy generated models for preview",
    )
    p_prepare.add_argument(
        "--overwrite-generated",
        action="store_true",
        help="Overwrite generated models in the pack",
    )
    p_prepare.add_argument(
        "--force-vanilla",
        action="store_true",
        help="Re-extract vanilla assets even if already present",
    )
    p_prepare.add_argument(
        "--minecraft-dir",
        type=Path,
        default=None,
        help="Path to .minecraft directory (Windows default is used when omitted)",
    )
    p_prepare.add_argument(
        "--minecraft-version",
        type=str,
        default=None,
        help="Minecraft version to locate the jar (default: gradle/libs.versions.toml)",
    )
    p_prepare.add_argument(
        "--minecraft-jar",
        type=Path,
        default=None,
        help="Direct path to a Minecraft client jar to extract assets from",
    )

    p_push = sub.add_parser("push", help="Copy edited files back into common resources")
    p_push.add_argument(
        "--pack-dir",
        type=Path,
        default=_default_pack_root(),
        help="Blockbench pack directory (default: run/blockbench_pack)",
    )
    p_push.add_argument(
        "--overwrite",
        action="store_true",
        help="Overwrite existing files under common resources",
    )

    return parser


def parse_args(argv: Optional[list[str]] | None = None) -> argparse.Namespace:
    return _build_parser().parse_args(argv)


def main(argv: Optional[list[str]] | None = None) -> None:
    ns = parse_args(argv)
    if ns.command == "prepare":
        prepare_pack(
            pack_root=ns.pack_dir,
            overwrite_mod=ns.overwrite_mod,
            overwrite_generated=ns.overwrite_generated,
            minecraft_dir=ns.minecraft_dir,
            minecraft_version=ns.minecraft_version,
            minecraft_jar=ns.minecraft_jar,
            force_vanilla=ns.force_vanilla,
            include_generated=not ns.no_generated,
        )
        return
    if ns.command == "push":
        push_pack(pack_root=ns.pack_dir, overwrite=ns.overwrite)
        return
    raise SystemExit(f"Unknown command: {ns.command}")


if __name__ == "__main__":
    main()
