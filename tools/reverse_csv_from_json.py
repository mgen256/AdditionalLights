#!/usr/bin/env python
from __future__ import annotations

import csv
from pathlib import Path
from tempfile import NamedTemporaryFile

from .common import read_csv_rows, write_csv_rows

ROOT = Path(__file__).resolve().parents[1]
BLOCKSTATE_DIR = (
    ROOT
    / "build"
    / "generated"
    / "datagen"
    / "resources"
    / "assets"
    / "additional_lights"
    / "blockstates"
)
CSV_PATH = ROOT / "tools" / "block_definitions.csv"
OUTPUT_PATH = (
    ROOT
    / "build"
    / "reports"
    / "datagen"
    / "block_definitions.from_json.csv"
)


SPECIAL = {
    "cobblestone": "CobbleStone",
    "mossy_cobblestone": "Mossy_CobbleStone",
    "blackstone": "BlackStone",
    "polished_blackstone": "Polished_BlackStone",
    "nether_bricks": "Nether_Bricks",
    "red_nether_bricks": "Red_Nether_Bricks",
    "stone_bricks": "Stone_Bricks",
    "mossy_stone_bricks": "Mossy_Stone_Bricks",
    "end_stone_bricks": "End_Stone_Bricks",
    "smooth_stone": "Smooth_Stone",
    "cut_sandstone": "Cut_Sand_Stone",
    "sandstone": "Sand_Stone",
    "polished_andesite": "Polished_Andesite",
    "polished_diorite": "Polished_Diorite",
    "polished_granite": "Polished_Granite",
    "diamond_block": "Diamond",
    "gold_block": "Gold",
    "iron_block": "Iron",
}


def camel(name: str) -> str:
    return "_".join(part.capitalize() for part in name.split("_"))


def convert(
    name: str,
    *,
    drop_planks: bool = False,
    packed_to_ice: bool = False,
    exclude_nether: bool = False,
) -> str:
    if drop_planks and name.endswith("_planks"):
        if exclude_nether and name in {"crimson_planks", "warped_planks"}:
            pass
        else:
            name = name[:-7]
    if packed_to_ice and name == "packed_ice":
        return "Ice"
    if name in {"crimson_planks", "warped_planks"}:
        return "".join(part.capitalize() for part in name.split("_"))
    if name in SPECIAL:
        return SPECIAL[name]
    return camel(name)


def reg_to_key(reg: str) -> str:
    if reg.startswith("al_lamp_"):
        body = reg[len("al_lamp_"):]
        return "ALLamp_" + convert(body, drop_planks=True, packed_to_ice=True, exclude_nether=True)
    if reg.startswith("al_wall_torch_"):
        body = reg[len("al_wall_torch_"):]
        return "ALTorch_Wall_" + convert(body, drop_planks=True, packed_to_ice=True)
    if reg.startswith("al_torch_"):
        body = reg[len("al_torch_"):]
        return "ALTorch_" + convert(body, drop_planks=True, packed_to_ice=True)
    if reg.startswith("standing_torch_s_"):
        body = reg[len("standing_torch_s_"):]
        return "StandingTorch_S_" + convert(body, packed_to_ice=False)
    if reg.startswith("standing_torch_l_"):
        body = reg[len("standing_torch_l_"):]
        return "StandingTorch_L_" + convert(body, packed_to_ice=False)
    if reg.startswith("fire_pit_s_"):
        body = reg[len("fire_pit_s_"):]
        return "FirePit_S_" + convert(body, drop_planks=True, packed_to_ice=True)
    if reg.startswith("fire_pit_l_"):
        body = reg[len("fire_pit_l_"):]
        return "FirePit_L_" + convert(body, drop_planks=True, packed_to_ice=True)
    if reg == "fire_for_standing_torch_s":
        return "Fire_For_StandingTorch_S"
    if reg == "fire_for_standing_torch_l":
        return "Fire_For_StandingTorch_L"
    if reg == "fire_for_fire_pit_s":
        return "Fire_For_FirePit_S"
    if reg == "fire_for_fire_pit_l":
        return "Fire_For_FirePit_L"
    if reg == "soul_fire_for_standing_torch_s":
        return "SoulFire_For_StandingTorch_S"
    if reg == "soul_fire_for_standing_torch_l":
        return "SoulFire_For_StandingTorch_L"
    if reg == "soul_fire_for_fire_pit_s":
        return "SoulFire_For_FirePit_S"
    if reg == "soul_fire_for_fire_pit_l":
        return "SoulFire_For_FirePit_L"
    if reg == "light_fire_for_standing_torch_s":
        return "LightFire_For_StandingTorch_S"
    if reg == "light_fire_for_standing_torch_l":
        return "LightFire_For_StandingTorch_L"
    if reg == "light_fire_for_fire_pit_s":
        return "LightFire_For_FirePit_S"
    if reg == "light_fire_for_fire_pit_l":
        return "LightFire_For_FirePit_L"
    raise ValueError(f"Unknown registry name: {reg}")


def collect_reg_names(blockstate_dir: Path = BLOCKSTATE_DIR) -> list[str]:
    if not blockstate_dir.is_dir():
        raise FileNotFoundError(
            f"Generated blockstate directory does not exist: {blockstate_dir}. "
            "Run './gradlew generateBlockData' first."
        )

    regs = sorted(path.stem for path in blockstate_dir.glob("*.json"))
    if not regs:
        raise ValueError(f"No blockstate JSON files found in {blockstate_dir}.")

    return regs


def load_cutout_metadata(csv_path: Path = CSV_PATH) -> dict[str, bool]:
    if not csv_path.is_file():
        raise FileNotFoundError(
            f"Canonical CSV does not exist: {csv_path}. "
            "The cutout column cannot be recovered from blockstate JSON."
        )

    with csv_path.open("r", encoding="utf-8", newline="") as csv_file:
        fieldnames = csv.DictReader(csv_file).fieldnames
    if fieldnames != ["key", "reg_name", "cutout"]:
        raise ValueError(
            f"Unexpected CSV schema in {csv_path}: {fieldnames}. "
            "Expected key, reg_name, cutout."
        )

    rows = read_csv_rows(csv_path)
    if not rows:
        raise ValueError(f"Canonical CSV contains no block definitions: {csv_path}.")

    cutout_by_reg: dict[str, bool] = {}
    for _key, reg_name, cutout in rows:
        if reg_name in cutout_by_reg:
            raise ValueError(f"Duplicate registry name in {csv_path}: {reg_name}.")
        cutout_by_reg[reg_name] = cutout
    return cutout_by_reg


def build_rows(
    reg_names: list[str],
    cutout_by_reg: dict[str, bool],
) -> list[tuple[str, str, bool]]:
    generated_names = set(reg_names)
    canonical_names = set(cutout_by_reg)
    missing_metadata = generated_names - canonical_names
    missing_blockstates = canonical_names - generated_names
    if missing_metadata or missing_blockstates:
        details: list[str] = []
        if missing_metadata:
            details.append(f"missing cutout metadata={sorted(missing_metadata)}")
        if missing_blockstates:
            details.append(f"missing generated blockstates={sorted(missing_blockstates)}")
        raise ValueError(
            "Generated blockstates and canonical CSV do not describe the same registrations: "
            + "; ".join(details)
        )

    return [
        (reg_to_key(reg_name), reg_name, cutout)
        for reg_name, cutout in cutout_by_reg.items()
    ]


def write_rows_atomically(
    output_path: Path,
    rows: list[tuple[str, str, bool]],
) -> None:
    with NamedTemporaryFile(
        prefix=f"{output_path.name}.",
        suffix=".tmp",
        dir=output_path.parent,
        delete=False,
    ) as temporary_file:
        temporary_path = Path(temporary_file.name)

    try:
        write_csv_rows(temporary_path, rows)
        temporary_path.replace(output_path)
    finally:
        temporary_path.unlink(missing_ok=True)


def main(
    *,
    blockstate_dir: Path = BLOCKSTATE_DIR,
    csv_path: Path = CSV_PATH,
    output_path: Path = OUTPUT_PATH,
) -> int:
    if output_path.resolve() == csv_path.resolve():
        raise ValueError(
            "The review output must not overwrite the canonical block_definitions.csv."
        )

    reg_names = collect_reg_names(blockstate_dir)
    rows = build_rows(reg_names, load_cutout_metadata(csv_path))
    output_path.parent.mkdir(parents=True, exist_ok=True)
    write_rows_atomically(output_path, rows)
    print(
        f"CSV review candidate created: rows={len(rows)}, "
        f"source={blockstate_dir}, canonical={csv_path}, output={output_path}"
    )
    return len(rows)


if __name__ == "__main__":
    main()
