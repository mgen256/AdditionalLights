
from __future__ import annotations

from typing import TYPE_CHECKING
from pathlib import Path

try:
    import typer as _typer
except ModuleNotFoundError:
    _typer = None

if TYPE_CHECKING:
    import typer

if _typer:
    app = _typer.Typer(help="Additional Lights development tools")

    @app.command("datagen")
    def datagen_cmd(
        check_only: bool = _typer.Option(False, help="Show diffs only"),
        gen_dir: Path | None = _typer.Option(None, help="Output directory for generated files"),
    ) -> None:
        from . import datagen as datagen_module

        datagen_module.main(check_only=check_only, gen_dir=gen_dir)

    @app.command("regenerate-csv")
    def regenerate_csv_cmd() -> None:
        from . import regenerate_csv as regenerate_csv_module

        regenerate_csv_module.main()

    @app.command("reverse-csv-from-json")
    def reverse_csv_from_json_cmd() -> None:
        from . import reverse_csv_from_json as reverse_csv_module

        reverse_csv_module.main()

    @app.command("naming-lint")
    def naming_lint_cmd(
        check_class_names: bool = _typer.Option(False, help="Also validate Python class names"),
        check_only: bool = _typer.Option(False, help="Check diffs only"),
    ) -> None:
        from . import naming_lint as naming_lint_module

        naming_lint_module.main(
            check_class_names=check_class_names, check_only=check_only
        )

    @app.command("loom-scan")
    def loom_scan_cmd(check_only: bool = _typer.Option(False, help="Run validation only")) -> None:
        from . import loom_scan as loom_scan_module

        loom_scan_module.main(check_only=check_only)

    @app.command("check-lang")
    def check_lang_cmd() -> None:
        from . import check_lang_consistency as lang_consistency_module

        lang_consistency_module.main()

    @app.command("asset-lint")
    def asset_lint_cmd(check_only: bool = _typer.Option(False, help="Run validation only")) -> None:
        from . import asset_lint as asset_lint_module

        asset_lint_module.main(check_only=check_only)

    def main() -> None:
        app()
else:
    app = None

    def main() -> None:
        raise ModuleNotFoundError("typer is required to run CLI commands")


if __name__ == "__main__":
    main()
