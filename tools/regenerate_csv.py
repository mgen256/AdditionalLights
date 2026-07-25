#!/usr/bin/env python
from __future__ import annotations

from .common import setup_logging
from .reverse_csv_from_json import main as reverse_main


def main() -> None:
    logger = setup_logging()
    logger.info("Creating a CSV review candidate from generated blockstates...")
    reverse_main()


if __name__ == "__main__":
    main()
