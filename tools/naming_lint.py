#!/usr/bin/env python
from __future__ import annotations

from pathlib import Path
from typing import Dict, Iterable, List, Tuple, Optional

import argparse
import ast
import re

import sys

from .reverse_csv_from_json import reg_to_key
from .common import repo_root, read_csv_rows, write_csv_rows, setup_logging

ROOT = repo_root()
CSV_FILE = ROOT / "tools" / "block_definitions.csv"

COMPAT_MAP: Dict[str, str] = {
}




def lint_rows(rows: List[Tuple[str, str, bool]]) -> Tuple[bool, List[Tuple[str, str, str]]]:
    updated = False
    errors: List[Tuple[str, str, str]] = []

    for i, (key, reg, _cutout) in enumerate(rows):
        expected = reg_to_key(reg)
        mapped = COMPAT_MAP.get(key, key)
        if mapped != expected:
            errors.append((key, reg, expected))
        if mapped != key:
            rows[i] = (mapped, reg)
            updated = True
    return updated, errors


CLASS_SUFFIXES = ("Core", "Abstract", "Behavior", "Trait", "Spec")
CLASS_PATTERN = re.compile(r"^[A-Z][A-Za-z0-9]*(?:" + "|".join(CLASS_SUFFIXES) + r")$")

EXCLUDE_DIRS = {"build"}


def should_exclude_python_path(path: Path) -> bool:
    return any(part in EXCLUDE_DIRS or part.startswith(".") for part in path.parts)


def python_files(root: Path) -> Iterable[Path]:

    for path in root.rglob("*.py"):
        if should_exclude_python_path(path):
            continue
        yield path


def lint_class_names(root: Path) -> List[Tuple[Path, int, str]]:
    errors: List[Tuple[Path, int, str]] = []
    for path in python_files(root):
        try:
            tree = ast.parse(path.read_text(encoding="utf-8"), filename=str(path))
        except SyntaxError:
            continue
        for node in ast.walk(tree):
            if isinstance(node, ast.ClassDef):
                name = node.name
                base_names = {getattr(b, "id", None) for b in node.bases}
                if "Enum" in base_names:
                    continue
                if name.startswith("I") and len(name) > 1 and name[1].isupper():
                    errors.append((path, node.lineno, name))
                elif not CLASS_PATTERN.match(name):
                    errors.append((path, node.lineno, name))
    return errors


def parse_args(argv: Optional[list[str]] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Lint CSV and class names")
    parser.add_argument(
        "--check-class-names",
        action="store_true",
        help="Also validate Python class names",
    )
    parser.add_argument(
        "--check-only",
        action="store_true",
        help="Check diffs only and exit non-zero on mismatches",
    )
    return parser.parse_args(argv)


def main(*, check_class_names: bool = False, check_only: bool = False) -> None:

    logger = setup_logging()
    rows = read_csv_rows(CSV_FILE)
    updated, errors = lint_rows(rows)

    exit_code = 0

    if errors:
        logger.error("Invalid key/reg_name pairs detected:")
        for key, reg, expected in errors:
            logger.error(" %s,%s -> expected key %s", key, reg, expected)
        exit_code = 1

    if updated:
        if check_only:
            logger.error("CSV requires update via COMPAT_MAP or key mismatch.")
            exit_code = 1
        else:
            write_csv_rows(CSV_FILE, rows)
            logger.info("CSV updated with migrated keys.")

    if exit_code == 0:
        logger.info("CSV naming check passed.")

    if check_class_names:
        class_errors = lint_class_names(ROOT)
        if class_errors:
            logger.error("Invalid class names detected:")
            for path, line, name in class_errors:
                logger.error(" %s:%d -> %s", path, line, name)
            exit_code = 1
        else:
            logger.info("Python class name check passed.")

    if exit_code:
        raise SystemExit(exit_code)


if __name__ == "__main__":
    ns = parse_args()
    main(check_class_names=ns.check_class_names, check_only=ns.check_only)
