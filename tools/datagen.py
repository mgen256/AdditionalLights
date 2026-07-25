#!/usr/bin/env python
from __future__ import annotations

import csv
import json
from pathlib import Path
import re
from typing import Iterable, Tuple, Set, Dict, List, Optional
import argparse
import difflib
import sys
from enum import Enum
from jinja2 import Environment, FileSystemLoader

ROOT = Path(__file__).resolve().parents[1]
CSV_PATH = ROOT / "tools" / "block_definitions.csv"
DEFAULT_GEN_ROOT = ROOT / "build" / "generated" / "datagen"
COMMON_ASSETS_BASE = (
    ROOT
    / "common"
    / "src"
    / "main"
    / "resources"
    / "assets"
    / "additional_lights"
)
COMMON_ITEM_ASSETS = COMMON_ASSETS_BASE / "items"
COMMON_BLOCKSTATE_ASSETS = COMMON_ASSETS_BASE / "blockstates"
COMMON_BLOCK_MODEL_ASSETS = COMMON_ASSETS_BASE / "models" / "block"

RESOURCE_BASE = COMMON_ITEM_ASSETS

TEMPLATE_DIR = Path(__file__).parent / "templates"
ENV = Environment(
    loader=FileSystemLoader(TEMPLATE_DIR),
    trim_blocks=True,
    lstrip_blocks=True,
)


class BlockGroup(Enum):

    LAMP = ("ALLamp_", "LampSpec")
    WALL_TORCH = ("ALTorch_Wall_", "WallTorchSpec")
    FLOOR_TORCH = ("ALTorch_", "FloorTorchSpec")
    STANDING_TORCH_S = ("StandingTorch_S_", "StandingTorchSSpec")
    STANDING_TORCH_L = ("StandingTorch_L_", "StandingTorchLSpec")
    FIRE_PIT_S = ("FirePit_S_", "FirePitSSpec")
    FIRE_PIT_L = ("FirePit_L_", "FirePitLSpec")
    FIRE_FOR = ("Fire_For_", "FireForSpec")
    SOUL_FIRE_FOR = ("SoulFire_For_", "SoulFireForSpec")
    LIGHT_FIRE_FOR = ("LightFire_For_", "LightFireForSpec")

    def __init__(self, prefix: str, enum_name: str) -> None:
        self.prefix = prefix
        self.enum_name = enum_name





def write_if_changed(path: Path, content: str) -> bool:
    if path.exists():
        old = path.read_text(encoding="utf-8")
        if old == content:
            return False
    else:
        path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")
    return True


def remove_extraneous(directory: Path, expected: Set[Path]) -> bool:
    changed = False
    if not directory.exists():
        return False
    for f in directory.iterdir():
        if f.is_file() and f not in expected:
            f.unlink()
            changed = True
    return changed


def read_definitions(path: Path) -> Iterable[Tuple[str, str, bool]]:
    with path.open("r", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        if not reader.fieldnames or not {"key", "reg_name"} <= set(reader.fieldnames):
            raise ValueError("CSV must contain 'key' and 'reg_name' columns")
        for row in reader:
            cutout = row.get("cutout", "false") == "true"
            yield row["key"], row["reg_name"], cutout


def categorize(defs: Iterable[Tuple[str, str, bool]]) -> Dict[str, List[Tuple[str, str, bool]]]:
    groups: Dict[str, List[Tuple[str, str, bool]]] = {g.enum_name: [] for g in BlockGroup}
    for idx, (key, reg, cutout) in enumerate(defs, 1):
        for g in BlockGroup:
            if key.startswith(g.prefix):
                groups[g.enum_name].append((key, reg, cutout))
                break
        else:
            raise ValueError(f"unknown key prefix at line {idx}: {key}")

    return {k: v for k, v in groups.items() if v}


def validate_torch_pairs(groups: Dict[str, List[Tuple[str, str, bool]]]) -> None:


    floor_defs = groups.get(BlockGroup.FLOOR_TORCH.enum_name, [])
    wall_defs = groups.get(BlockGroup.WALL_TORCH.enum_name, [])

    if not floor_defs and not wall_defs:
        return

    floor_keys = {k.replace("ALTorch_", "") for k, _, _ in floor_defs}
    wall_keys = {k.replace("ALTorch_Wall_", "") for k, _, _ in wall_defs}

    missing_floor = wall_keys - floor_keys
    missing_wall = floor_keys - wall_keys

    if missing_floor or missing_wall:
        msg_parts = []
        if missing_floor:
            msg_parts.append("missing floor: " + ", ".join(sorted(missing_floor)))
        if missing_wall:
            msg_parts.append("missing wall: " + ", ".join(sorted(missing_wall)))
        raise ValueError("; ".join(msg_parts))


def generate_interface(names: Iterable[str]) -> str:
    perms = [n for n in names if n not in ("FloorTorchSpec", "WallTorchSpec")]
    perms.insert(0, "AbstractTorchSpec")
    tmpl = ENV.get_template("BlockSpec.java.j2")
    return tmpl.render(perms=perms)


def generate_modblocklist(enum_names: Iterable[str]) -> str:
    tmpl = ENV.get_template("ModBlockList.java.j2")
    return tmpl.render(enum_names=list(enum_names))


def generate_abstract_torch() -> str:
    tmpl = ENV.get_template("AbstractTorchSpec.java.j2")
    return tmpl.render()


def generate_enum(name: str, defs: List[Tuple[str, str, bool]]) -> str:
    consts: List[str] = []
    for key, reg, cutout in defs:
        if name == "WallTorchSpec":
            floor = key.replace("ALTorch_Wall_", "ALTorch_")
            consts.append(f"{key}(\"{key}\", \"{reg}\", \"{floor}\", {str(cutout).lower()})")
        elif name == "FloorTorchSpec":
            wall = key.replace("ALTorch_", "ALTorch_Wall_")
            consts.append(f"{key}(\"{key}\", \"{reg}\", \"{wall}\", {str(cutout).lower()})")
        else:
            consts.append(f"{key}(\"{key}\", \"{reg}\", {str(cutout).lower()})")
    extra = "floorKey" if name == "WallTorchSpec" else "wallKey"
    tmpl = ENV.get_template("Enum.java.j2")
    return tmpl.render(name=name, consts=consts, extra=extra)


def resolve_item_model_id(reg_name: str) -> str | None:



    if "al_wall_torch" in reg_name:
        return None

    if reg_name.startswith("al_lamp_"):
        model_name = reg_name.replace("al_lamp_", "al_wall_lamp_", 1)
        return f"additional_lights:block/al_lamp/{model_name}"

    if reg_name.startswith("al_torch_"):
        return f"additional_lights:block/al_torch/{reg_name}"

    if reg_name.startswith("standing_torch_s_"):
        return f"additional_lights:block/standing_torch_s/{reg_name}"

    if reg_name.startswith("standing_torch_l_"):
        return f"additional_lights:block/standing_torch_l/{reg_name}"

    if reg_name.startswith("fire_pit_s_"):
        return f"additional_lights:block/fire_pit_s/{reg_name}"

    if reg_name.startswith("fire_pit_l_"):
        return f"additional_lights:block/fire_pit_l/{reg_name}"

    if reg_name.startswith("light_fire_for_"):
        return None

    if reg_name.startswith(("fire_for_", "soul_fire_for_")):
        return f"additional_lights:block/fire/{reg_name}"

    raise ValueError(f"unknown reg_name pattern: {reg_name}")


def generate_item_json(reg_name: str, *, model_id: str | None = None) -> str:
    resolved = model_id if model_id is not None else resolve_item_model_id(reg_name)
    if resolved is None:
        raise ValueError(f"item json is not expected for {reg_name}")
    tmpl = ENV.get_template("item.json.j2")
    return tmpl.render(model_id=resolved)


def _render_json(data: object) -> str:
    return json.dumps(data, ensure_ascii=False, indent=2) + "\n"


def _strip_prefix(value: str, prefix: str) -> str:
    if not value.startswith(prefix):
        raise ValueError(f"expected prefix '{prefix}' for '{value}'")
    return value[len(prefix) :]


def generate_blockstate_json(reg_name: str) -> str:
    if reg_name.startswith("al_lamp_"):
        base = _strip_prefix(reg_name, "al_lamp_")
        lamp_model = f"additional_lights:block/al_lamp/{reg_name}"
        wall_model = f"additional_lights:block/al_lamp/al_wall_lamp_{base}"
        variants = {
            "facing=up": {"model": lamp_model},
            "facing=down": {"model": lamp_model, "x": 180},
            "facing=east": {"model": wall_model, "y": 0},
            "facing=south": {"model": wall_model, "y": 90},
            "facing=west": {"model": wall_model, "y": 180},
            "facing=north": {"model": wall_model, "y": 270},
        }
        return _render_json({"variants": variants})

    if reg_name.startswith("al_torch_"):
        base = _strip_prefix(reg_name, "al_torch_")
        normal = f"additional_lights:block/al_torch/{reg_name}"
        soul = f"additional_lights:block/al_torch/al_soul_torch_{base}"
        light = f"additional_lights:block/al_torch/al_light_torch_{base}"
        return _render_json(
            {
                "variants": {
                    "firetype=normal": {"model": normal},
                    "firetype=soul": {"model": soul},
                    "firetype=light": {"model": light},
                }
            }
        )

    if reg_name.startswith("al_wall_torch_"):
        base = _strip_prefix(reg_name, "al_wall_torch_")
        normal = f"additional_lights:block/al_torch/{reg_name}"
        soul = f"additional_lights:block/al_torch/al_soul_wall_torch_{base}"
        light = f"additional_lights:block/al_torch/al_light_wall_torch_{base}"
        variants = {
            "facing=east,firetype=normal": {"model": normal},
            "facing=south,firetype=normal": {"model": normal, "y": 90},
            "facing=west,firetype=normal": {"model": normal, "y": 180},
            "facing=north,firetype=normal": {"model": normal, "y": 270},
            "facing=east,firetype=soul": {"model": soul},
            "facing=south,firetype=soul": {"model": soul, "y": 90},
            "facing=west,firetype=soul": {"model": soul, "y": 180},
            "facing=north,firetype=soul": {"model": soul, "y": 270},
            "facing=east,firetype=light": {"model": light},
            "facing=south,firetype=light": {"model": light, "y": 90},
            "facing=west,firetype=light": {"model": light, "y": 180},
            "facing=north,firetype=light": {"model": light, "y": 270},
        }
        return _render_json({"variants": variants})

    if reg_name.startswith("standing_torch_s_"):
        normal = f"additional_lights:block/standing_torch_s/{reg_name}"
        soul = f"additional_lights:block/standing_torch_s/{reg_name}_soulfire"
        return _render_json({"variants": _pedestal_fire_variants(normal, soul)})

    if reg_name.startswith("standing_torch_l_"):
        normal = f"additional_lights:block/standing_torch_l/{reg_name}"
        soul = f"additional_lights:block/standing_torch_l/{reg_name}_soulfire"
        return _render_json({"variants": _pedestal_fire_variants(normal, soul)})

    if reg_name.startswith("fire_pit_s_"):
        normal = f"additional_lights:block/fire_pit_s/{reg_name}"
        soul = f"additional_lights:block/fire_pit_s/{reg_name}_soulfire"
        return _render_json({"variants": _pedestal_fire_variants(normal, soul)})

    if reg_name.startswith("fire_pit_l_"):
        normal = f"additional_lights:block/fire_pit_l/{reg_name}"
        soul = f"additional_lights:block/fire_pit_l/{reg_name}_soulfire"
        return _render_json({"variants": _pedestal_fire_variants(normal, soul)})

    if reg_name.startswith(("fire_for_", "soul_fire_for_", "light_fire_for_")):
        normal = f"additional_lights:block/fire/{reg_name}"
        set_model = f"additional_lights:block/fire/{reg_name}_set"
        return _render_json(
            {
                "variants": {
                    "set=false": {"model": normal},
                    "set=true": {"model": set_model},
                }
            }
        )

    raise ValueError(f"unknown reg_name pattern: {reg_name}")


def _pedestal_fire_variants(normal: str, soul: str) -> Dict[str, Dict[str, str]]:
    return {
        "firetype=normal": {"model": normal},
        "firetype=soul": {"model": soul},
        "firetype=light": {"model": normal},
    }


def _model_json(*, parent: str, textures: Dict[str, str]) -> str:
    return _render_json({"parent": parent, "textures": textures})


def _lamp_floor_parent(base: str) -> str:
    if base == "glass":
        return "additional_lights:block/al_lamp/template_al_lamp_glass"
    if base == "packed_ice":
        return "additional_lights:block/al_lamp/template_al_lamp_ice"
    if base == "quartz_block":
        return "additional_lights:block/al_lamp/template_al_lamp_quartz"
    if base in {"iron_block", "gold_block", "diamond_block"}:
        return "additional_lights:block/al_lamp/template_al_lamp_smooth"
    if base in {"nether_bricks", "red_nether_bricks"}:
        return "additional_lights:block/al_lamp/template_al_lamp_nether"
    if base.endswith(("_planks", "_wool")):
        return "additional_lights:block/al_lamp/template_al_lamp_planks"
    return "additional_lights:block/al_lamp/template_al_lamp_stone"


def _lamp_wall_parent(base: str) -> str:
    if base == "glass":
        return "additional_lights:block/al_lamp/template_al_wall_lamp_glass"
    if base == "packed_ice":
        return "additional_lights:block/al_lamp/template_al_wall_lamp_ice"
    if base == "quartz_block":
        return "additional_lights:block/al_lamp/template_al_wall_lamp_quartz"
    if base in {"iron_block", "gold_block", "diamond_block"}:
        return "additional_lights:block/al_lamp/template_al_wall_lamp_smooth"
    if base.endswith("_planks"):
        return "additional_lights:block/al_lamp/template_al_wall_lamp_planks"
    if base.endswith("_wool"):
        return "additional_lights:block/al_lamp/template_al_wall_lamp_wool"
    if base in {"cobblestone", "mossy_cobblestone"}:
        return "additional_lights:block/al_lamp/template_al_wall_lamp_cobblestone"
    return "additional_lights:block/al_lamp/template_al_wall_lamp_stone"


def _base_block_main_texture(base: str) -> str:
    if base == "quartz_block":
        return "block/quartz_block_side"
    return f"block/{base}"


def _lamp_floor_texture(base: str) -> str:
    if base == "blackstone":
        return "block/blackstone_top"
    return _base_block_main_texture(base)


def _lamp_wall_texture(base: str) -> str:
    if base == "glass":
        return "additional_lights:block/glass"
    return _base_block_main_texture(base)


def _torch_main_texture(base: str) -> str:
    if base == "glass":
        return "additional_lights:block/glass"
    if base == "blackstone":
        return "block/polished_blackstone"
    return _base_block_main_texture(base)


def _torch_floor_parent(base: str) -> str:
    if base.endswith("_planks"):
        return "additional_lights:block/al_torch/template_al_torch_planks"
    if base in {"nether_bricks", "red_nether_bricks"}:
        return "additional_lights:block/al_torch/template_al_torch_nether_bricks"
    if base == "glass":
        return "additional_lights:block/al_torch/template_al_torch_glass"
    if base == "quartz_block":
        return "additional_lights:block/al_torch/template_al_torch_quartz"
    if base in {
        "blackstone",
        "diamond_block",
        "gold_block",
        "iron_block",
        "stone_bricks",
        "mossy_stone_bricks",
        "end_stone_bricks",
        "smooth_stone",
    }:
        return "additional_lights:block/al_torch/template_al_torch_smooth"
    return "additional_lights:block/al_torch/template_al_torch_stone"


def _torch_floor_light_parent(base: str) -> str:
    return _torch_floor_parent(base) + "_light"


def _torch_wall_parent(base: str) -> str:
    if base in {"gold_block", "diamond_block"}:
        return "additional_lights:block/al_torch/template_al_wall_torch_gold"
    if base == "end_stone_bricks":
        return "additional_lights:block/al_torch/template_al_wall_torch_end_stone_bricks"
    if base.endswith("_planks"):
        return "additional_lights:block/al_torch/template_al_wall_torch_planks"
    if base in {"nether_bricks", "red_nether_bricks"}:
        return "additional_lights:block/al_torch/template_al_wall_torch_nether_bricks"
    if base == "glass":
        return "additional_lights:block/al_torch/template_al_wall_torch_glass"
    if base == "quartz_block":
        return "additional_lights:block/al_torch/template_al_wall_torch_quartz"
    if base in {
        "blackstone",
        "iron_block",
        "stone_bricks",
        "mossy_stone_bricks",
        "smooth_stone",
    }:
        return "additional_lights:block/al_torch/template_al_wall_torch_smooth"
    return "additional_lights:block/al_torch/template_al_wall_torch_stone"


def _torch_wall_light_parent(base: str) -> str:
    return _torch_wall_parent(base) + "_light"


def _standing_torch_parent(base: str, *, size: str) -> str:
    if size not in {"s", "l"}:
        raise ValueError(f"unknown standing torch size: {size}")
    prefix = f"additional_lights:block/standing_torch_{size}/template_standing_torch_{size}_"
    if base == "cut_sandstone":
        return prefix + "cut"
    if base == "sandstone":
        return prefix + "sandstone"
    if base == "end_stone_bricks":
        return prefix + "end_stone_bricks"
    if base in {"stone_bricks", "mossy_stone_bricks", "quartz_bricks"}:
        return prefix + "stone_bricks"
    if base in {"nether_bricks", "red_nether_bricks"}:
        return prefix + "nether_bricks"
    if base in {"polished_andesite", "polished_diorite", "polished_granite", "polished_blackstone"}:
        return prefix + "polished"
    if base == "quartz_block":
        return prefix + "quartz"
    if base in {
        "smooth_stone",
        "cobblestone",
        "mossy_cobblestone",
        "iron_block",
        "gold_block",
        "diamond_block",
    }:
        return prefix + "smooth"
    return prefix + "stone"


def _fire_pit_parent(base: str, *, size: str) -> str:
    if size not in {"s", "l"}:
        raise ValueError(f"unknown fire pit size: {size}")
    prefix = f"additional_lights:block/fire_pit_{size}/template_fire_pit_{size}_"
    if base == "cut_sandstone":
        return prefix + "cut"
    if base == "sandstone":
        return prefix + "sandstone"
    if base in {"stone_bricks", "mossy_stone_bricks", "end_stone_bricks", "quartz_bricks"}:
        return prefix + "stone_bricks"
    if base in {"nether_bricks", "red_nether_bricks"}:
        return prefix + "nether_bricks"
    if base in {"polished_andesite", "polished_diorite", "polished_granite", "polished_blackstone"}:
        return prefix + "polished"
    if base == "quartz_block":
        return prefix + "quartz"
    if base in {"smooth_stone", "iron_block", "gold_block", "diamond_block"}:
        return prefix + "smooth"
    return prefix + "stone"


def generate_block_model_jsons(reg_name: str) -> Dict[Path, str]:

    if reg_name.startswith("al_lamp_"):
        base = _strip_prefix(reg_name, "al_lamp_")
        wall_name = f"al_wall_lamp_{base}"
        return {
            Path("al_lamp") / f"{reg_name}.json": _model_json(
                parent=_lamp_floor_parent(base),
                textures={"main": _lamp_floor_texture(base)},
            ),
            Path("al_lamp") / f"{wall_name}.json": _model_json(
                parent=_lamp_wall_parent(base),
                textures={"main": _lamp_wall_texture(base)},
            ),
        }

    if reg_name.startswith("al_torch_"):
        base = _strip_prefix(reg_name, "al_torch_")
        return {
            Path("al_torch") / f"{reg_name}.json": _model_json(
                parent=_torch_floor_parent(base),
                textures={"main": _torch_main_texture(base)},
            ),
            Path("al_torch") / f"al_soul_torch_{base}.json": _model_json(
                parent=f"additional_lights:block/al_torch/{reg_name}",
                textures={"texture": "block/soul_torch", "particle": "block/soul_torch"},
            ),
            Path("al_torch") / f"al_light_torch_{base}.json": _model_json(
                parent=_torch_floor_light_parent(base),
                textures={"main": _torch_main_texture(base)},
            ),
        }

    if reg_name.startswith("al_wall_torch_"):
        base = _strip_prefix(reg_name, "al_wall_torch_")
        return {
            Path("al_torch") / f"{reg_name}.json": _model_json(
                parent=_torch_wall_parent(base),
                textures={"main": _torch_main_texture(base)},
            ),
            Path("al_torch") / f"al_soul_wall_torch_{base}.json": _model_json(
                parent=f"additional_lights:block/al_torch/{reg_name}",
                textures={"texture": "block/soul_torch", "particle": "block/soul_torch"},
            ),
            Path("al_torch") / f"al_light_wall_torch_{base}.json": _model_json(
                parent=_torch_wall_light_parent(base),
                textures={"main": _torch_main_texture(base)},
            ),
        }

    if reg_name.startswith("standing_torch_s_"):
        base = _strip_prefix(reg_name, "standing_torch_s_")
        return {
            Path("standing_torch_s") / f"{reg_name}.json": _model_json(
                parent=_standing_torch_parent(base, size="s"),
                textures={"main": _base_block_main_texture(base)},
            ),
            Path("standing_torch_s") / f"{reg_name}_soulfire.json": _model_json(
                parent=f"additional_lights:block/standing_torch_s/{reg_name}",
                textures={"texture2": "block/soul_sand"},
            ),
        }

    if reg_name.startswith("standing_torch_l_"):
        base = _strip_prefix(reg_name, "standing_torch_l_")
        return {
            Path("standing_torch_l") / f"{reg_name}.json": _model_json(
                parent=_standing_torch_parent(base, size="l"),
                textures={"main": _base_block_main_texture(base)},
            ),
            Path("standing_torch_l") / f"{reg_name}_soulfire.json": _model_json(
                parent=f"additional_lights:block/standing_torch_l/{reg_name}",
                textures={"texture2": "block/soul_sand"},
            ),
        }

    if reg_name.startswith("fire_pit_s_"):
        base = _strip_prefix(reg_name, "fire_pit_s_")
        return {
            Path("fire_pit_s") / f"{reg_name}.json": _model_json(
                parent=_fire_pit_parent(base, size="s"),
                textures={"main": _base_block_main_texture(base)},
            ),
            Path("fire_pit_s") / f"{reg_name}_soulfire.json": _model_json(
                parent=f"additional_lights:block/fire_pit_s/{reg_name}",
                textures={"texture2": "block/soul_sand"},
            ),
        }

    if reg_name.startswith("fire_pit_l_"):
        base = _strip_prefix(reg_name, "fire_pit_l_")
        return {
            Path("fire_pit_l") / f"{reg_name}.json": _model_json(
                parent=_fire_pit_parent(base, size="l"),
                textures={"main": _base_block_main_texture(base)},
            ),
            Path("fire_pit_l") / f"{reg_name}_soulfire.json": _model_json(
                parent=f"additional_lights:block/fire_pit_l/{reg_name}",
                textures={"texture2": "block/soul_sand"},
            ),
        }

    if reg_name.startswith(("fire_for_", "soul_fire_for_", "light_fire_for_")):
        return {}

    raise ValueError(f"unknown reg_name pattern: {reg_name}")


def remove_extraneous_recursive(directory: Path, expected: Set[Path]) -> bool:
    changed = False
    if not directory.exists():
        return False
    for f in directory.rglob("*"):
        if f.is_file() and f not in expected:
            f.unlink()
            changed = True
    return changed


def base_field_name(reg_name: str) -> str | None:
    if reg_name.startswith(("fire_for_", "soul_fire_for_", "light_fire_for_")):
        return None

    base = re.sub(r"^al_lamp_", "", reg_name)
    base = re.sub(r"^al_wall_(?:lamp|torch)_", "", base)
    base = re.sub(r"^al_torch_", "", base)
    base = re.sub(r"^standing_torch_[sl]_", "", base)
    base = re.sub(r"^fire_pit_[sl]_", "", base)
    return base.upper()


def generate_vanilla_block_map(defs: Iterable[Tuple[str, str, bool]]) -> str:
    entries = []
    for _, reg, _ in defs:
        field = base_field_name(reg)
        if field is not None:
            entries.append((reg, field))
    tmpl = ENV.get_template("VanillaBlockMap.java.j2")
    return tmpl.render(entries=entries)


def diff_text(old: str, new: str, *, path: Path) -> None:
    diff = difflib.unified_diff(
        old.splitlines(),
        new.splitlines(),
        fromfile=str(path),
        tofile="generated",
        lineterm="",
    )
    for line in diff:
        print(line)


def parse_args(argv: Optional[list[str]] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Generate block data")
    parser.add_argument(
        "--check-only",
        action="store_true",
        help="Show diffs only without updating files",
    )
    parser.add_argument(
        "--gen-dir",
        type=Path,
        default=None,
        help="Override the generated output directory (default: build/generated/datagen)",
    )
    return parser.parse_args(argv)


def main(*, check_only: bool = False, gen_dir: Path | None = None) -> None:

    gen_root = gen_dir or DEFAULT_GEN_ROOT
    java_out = gen_root / "java" / "com" / "mgen256" / "al"
    resource_root = gen_root / "resources" / "assets" / "additional_lights"
    item_out = resource_root / "items"
    blockstate_out = resource_root / "blockstates"
    model_out = resource_root / "models" / "block"

    defs = list(read_definitions(CSV_PATH))
    groups = categorize(defs)
    validate_torch_pairs(groups)

    sources: Dict[Path, str] = {}
    interface_code = generate_interface(groups.keys())
    sources[java_out / "BlockSpec.java"] = interface_code
    if "FloorTorchSpec" in groups or "WallTorchSpec" in groups:
        sources[java_out / "AbstractTorchSpec.java"] = generate_abstract_torch()
    sources[java_out / "ModBlockList.java"] = generate_modblocklist(groups.keys())
    sources[java_out / "VanillaBlockMap.java"] = generate_vanilla_block_map(defs)
    for name, items in groups.items():
        sources[java_out / f"{name}.java"] = generate_enum(name, items)

    if check_only:
        ok = True
        for path, code in sources.items():
            if path.exists():
                old = path.read_text(encoding="utf-8")
            else:
                old = ""
            if old != code:
                diff_text(old, code, path=path)
                ok = False
        expected_java = set(sources.keys())
        for extra in java_out.glob("*.java"):
            if extra not in expected_java:
                diff_text(extra.read_text(encoding="utf-8"), "", path=extra)
                ok = False

        expected_item_jsons: Set[Path] = set()
        for _, reg_name, _ in defs:
            model_id = resolve_item_model_id(reg_name)
            if model_id is None:
                continue
            if (RESOURCE_BASE / f"{reg_name}.json").exists():
                continue
            path = item_out / f"{reg_name}.json"
            expected_item_jsons.add(path)
            new_text = generate_item_json(reg_name, model_id=model_id)
            if path.exists():
                old_text = path.read_text(encoding="utf-8")
            else:
                old_text = ""
            if old_text != new_text:
                diff_text(old_text, new_text, path=path)
                ok = False

        for extra in item_out.glob("*.json"):
            if extra not in expected_item_jsons:
                diff_text(extra.read_text(encoding="utf-8"), "", path=extra)
                ok = False

        expected_blockstates: Set[Path] = set()
        for _, reg_name, _ in defs:
            if (COMMON_BLOCKSTATE_ASSETS / f"{reg_name}.json").exists():
                continue
            path = blockstate_out / f"{reg_name}.json"
            expected_blockstates.add(path)
            new_text = generate_blockstate_json(reg_name)
            old_text = path.read_text(encoding="utf-8") if path.exists() else ""
            if old_text != new_text:
                diff_text(old_text, new_text, path=path)
                ok = False

        for extra in blockstate_out.glob("*.json"):
            if extra not in expected_blockstates:
                diff_text(extra.read_text(encoding="utf-8"), "", path=extra)
                ok = False

        expected_models: Set[Path] = set()
        for _, reg_name, _ in defs:
            for rel_path, new_text in generate_block_model_jsons(reg_name).items():
                manual = COMMON_BLOCK_MODEL_ASSETS / rel_path
                if manual.exists():
                    continue
                path = model_out / rel_path
                expected_models.add(path)
                old_text = path.read_text(encoding="utf-8") if path.exists() else ""
                if old_text != new_text:
                    diff_text(old_text, new_text, path=path)
                    ok = False

        for extra in model_out.rglob("*.json"):
            if extra not in expected_models:
                diff_text(extra.read_text(encoding="utf-8"), "", path=extra)
                ok = False

        if not ok:
            raise SystemExit(1)
        return

    java_out.mkdir(parents=True, exist_ok=True)
    item_out.mkdir(parents=True, exist_ok=True)
    blockstate_out.mkdir(parents=True, exist_ok=True)
    model_out.mkdir(parents=True, exist_ok=True)

    changed = False
    for path, code in sources.items():
        changed |= write_if_changed(path, code)

    expected_item_jsons: Set[Path] = set()
    for _, reg_name, _ in defs:
        model_id = resolve_item_model_id(reg_name)
        if model_id is None:
            continue
        if (RESOURCE_BASE / f"{reg_name}.json").exists():
            continue
        target = item_out / f"{reg_name}.json"
        expected_item_jsons.add(target)
        json_text = generate_item_json(reg_name, model_id=model_id)
        changed |= write_if_changed(target, json_text)

    expected_blockstates: Set[Path] = set()
    for _, reg_name, _ in defs:
        if (COMMON_BLOCKSTATE_ASSETS / f"{reg_name}.json").exists():
            continue
        target = blockstate_out / f"{reg_name}.json"
        expected_blockstates.add(target)
        json_text = generate_blockstate_json(reg_name)
        changed |= write_if_changed(target, json_text)

    expected_models: Set[Path] = set()
    for _, reg_name, _ in defs:
        for rel_path, json_text in generate_block_model_jsons(reg_name).items():
            manual = COMMON_BLOCK_MODEL_ASSETS / rel_path
            if manual.exists():
                continue
            target = model_out / rel_path
            expected_models.add(target)
            changed |= write_if_changed(target, json_text)

    changed |= remove_extraneous(java_out, set(sources.keys()))
    changed |= remove_extraneous(item_out, expected_item_jsons)
    changed |= remove_extraneous(blockstate_out, expected_blockstates)
    changed |= remove_extraneous_recursive(model_out, expected_models)

    if not changed:
        return


if __name__ == "__main__":
    args = parse_args()
    main(check_only=args.check_only, gen_dir=args.gen_dir)
