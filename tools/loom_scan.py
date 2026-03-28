#!/usr/bin/env python

from __future__ import annotations

from pathlib import Path
from typing import Iterable, List, Optional
import argparse

from .common import repo_root, setup_logging, json_validate


EXCLUDED_DIRS = {
    "run",
    "runs",
    "downloads",
    ".gradle",
    ".venv",
    "run-data",
    "build",
}


def collect_json_files(root: Path) -> Iterable[Path]:
    for path in root.rglob("*.json"):
        if any(part in EXCLUDED_DIRS for part in path.parts):
            continue
        yield path



def scan_files(paths: Iterable[Path]) -> List[tuple[Path, str]]:
    invalid: List[tuple[Path, str]] = []
    for p in paths:
        ok, msg = json_validate(p)
        if not ok:
            invalid.append((p, msg or "unknown error"))
    return invalid


def parse_args(argv: Optional[list[str]] | None = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Validate JSON files")
    parser.add_argument(
        "--check-only",
        action="store_true",
        help="Run validation only and exit non-zero on errors",
    )
    return parser.parse_args(argv)


def main(*, check_only: bool = False) -> None:

    logger = setup_logging()
    root = repo_root()
    json_files = list(collect_json_files(root))
    invalid = scan_files(json_files)
    if invalid:
        RED = "\x1b[31m"
        RESET = "\x1b[0m"
        file, msg = invalid[0]
        logger.error("%sJSON ERROR%s %s \u2192 %s", RED, RESET, file.relative_to(root), msg)
        if len(invalid) > 1:
            logger.error("\u2026and %d more invalid JSON files", len(invalid) - 1)
        raise SystemExit(1)
    logger.info("JSON validation passed.")


if __name__ == "__main__":
    ns = parse_args()
    main(check_only=ns.check_only)
