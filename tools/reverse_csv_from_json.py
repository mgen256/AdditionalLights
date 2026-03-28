#!/usr/bin/env python
from __future__ import annotations

import csv
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BLOCKSTATE_DIR = ROOT / "core" / "src" / "main" / "resources" / "assets" / "additional_lights" / "blockstates"
CSV_PATH = ROOT / "tools" / "block_definitions.csv"


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
    raise ValueError(f"Unknown registry name: {reg}")


def collect_reg_names() -> list[str]:
    regs: list[str] = []
    for p in BLOCKSTATE_DIR.glob("*.json"):
        regs.append(p.stem)
    return regs


def main() -> None:
    regs = collect_reg_names()
    rows = [(reg_to_key(r), r) for r in regs]
    with CSV_PATH.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["key", "reg_name"])
        writer.writerows(rows)


if __name__ == "__main__":
    main()
