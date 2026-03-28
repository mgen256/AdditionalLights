#!/usr/bin/env python
from __future__ import annotations

import json
from pathlib import Path
from typing import Dict, Set
import re

from .common import repo_root, setup_logging


LANG_DIR = (
    repo_root()
    / "common"
    / "src"
    / "main"
    / "resources"
    / "assets"
    / "additional_lights"
    / "lang"
)


def _decode_no_duplicates(pairs: list[tuple[str, str]]) -> Dict[str, str]:
    obj: Dict[str, str] = {}
    for key, value in pairs:
        if key in obj:
            raise ValueError(f"duplicate key: {key}")
        obj[key] = value
    return obj


def load_json(path: Path) -> Dict[str, str]:

    text = path.read_text(encoding="utf-8")
    return json.loads(text, object_pairs_hook=_decode_no_duplicates)


_FMT_RE = re.compile(r"%(?!%)(?:\d+\$)?[\w]")


def count_fmt(text: str) -> int:
    return len(_FMT_RE.findall(text))


def check_lang(lang_dir: Path) -> Dict[str, Dict[str, Set[str]]]:
    en_data = load_json(lang_dir / "en_us.json")
    en_keys = set(en_data.keys())
    result: Dict[str, Dict[str, Set[str]]] = {}
    for path in lang_dir.glob("*.json"):
        if path.name == "en_us.json":
            continue
        data = load_json(path)
        keys = set(data.keys())
        missing = en_keys - keys
        extra = keys - en_keys
        empty = {k for k in en_keys & keys if data[k] == ""}
        fmt = {
            k
            for k in en_keys & keys
            if count_fmt(en_data[k]) != count_fmt(data[k])
        }
        issues = {}
        if missing:
            issues["missing"] = missing
        if extra:
            issues["extra"] = extra
        if empty:
            issues["empty"] = empty
        if fmt:
            issues["format"] = fmt
        if issues:
            result[path.name] = issues
    return result


def main() -> None:
    logger = setup_logging()
    inconsistencies = check_lang(LANG_DIR)
    if inconsistencies:
        for name, diff in inconsistencies.items():
            if diff.get("missing"):
                logger.error(
                    "%s: missing keys: %s",
                    name,
                    ", ".join(sorted(diff["missing"])),
                )
            if diff.get("extra"):
                logger.error(
                    "%s: extra keys: %s",
                    name,
                    ", ".join(sorted(diff["extra"])),
                )
            if diff.get("empty"):
                logger.error(
                    "%s: empty values: %s",
                    name,
                    ", ".join(sorted(diff["empty"])),
                )
            if diff.get("format"):
                logger.error(
                    "%s: format mismatch: %s",
                    name,
                    ", ".join(sorted(diff["format"])),
                )
        raise SystemExit(1)
    logger.info("Language files are consistent.")


if __name__ == "__main__":
    main()
