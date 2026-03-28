from __future__ import annotations

import csv
import json
import logging
import subprocess
from pathlib import Path
from typing import Any, Iterable, List, Tuple


def setup_logging(level: int = logging.INFO) -> logging.Logger:
    logging.basicConfig(level=level, format="%(levelname)s: %(message)s")
    return logging.getLogger(__name__)


def repo_root() -> Path:
    return Path(__file__).resolve().parents[2]


def read_csv_rows(path: Path) -> List[Tuple[str, str, bool]]:
    with path.open("r", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        return [
            (row["key"], row["reg_name"], row.get("cutout", "false") == "true")
            for row in reader
        ]


def write_csv_rows(path: Path, rows: Iterable[Tuple[str, str, bool]]) -> None:
    with path.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["key", "reg_name", "cutout"])
        writer.writerows(
            (key, reg, "true" if cutout else "false") for key, reg, cutout in rows
        )


def run_subprocess(cmd: List[str]) -> int:
    result = subprocess.run(cmd, check=False)
    return result.returncode


def read_json(path: Path) -> Any:
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def json_validate(path: Path) -> tuple[bool, str | None]:

    try:
        json.loads(path.read_text(encoding="utf-8"))
        return True, None
    except json.JSONDecodeError as e:
        return False, f"{e.msg} (line {e.lineno}, col {e.colno})"

__all__ = [
    "setup_logging",
    "repo_root",
    "read_csv_rows",
    "write_csv_rows",
    "run_subprocess",
    "read_json",
    "json_validate",
]
