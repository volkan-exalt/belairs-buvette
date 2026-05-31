#!/usr/bin/env python3
import re
import sys
from pathlib import Path

REQUIRED_HEADERS = [
    "## Context",
    "## Impacted layer",
    "## Acceptance criteria",
    "## Gherkin scenarios",
    "## Edge cases",
    "## Validation",
]


def fail(path, message):
    print(f"{path}: {message}", file=sys.stderr)
    return 1


def validate(path):
    issue_path = Path(path)
    if not issue_path.exists():
        return fail(issue_path, "file does not exist")

    content = issue_path.read_text(encoding="utf-8")
    if not re.search(r"^# .+", content, re.MULTILINE):
        return fail(issue_path, "missing H1 title")

    for header in REQUIRED_HEADERS:
        if header not in content:
            return fail(issue_path, f"missing required header: {header}")

    if not re.search(r"^- \[ \] .+", content, re.MULTILINE):
        return fail(issue_path, "missing unchecked checklist item")

    if "```gherkin" not in content:
        return fail(issue_path, "missing gherkin fenced block")

    if len(re.findall(r"Scenario:", content)) < 2:
        return fail(issue_path, "expected at least two Gherkin scenarios")

    for keyword in ["Given ", "When ", "Then "]:
        if keyword not in content:
            return fail(issue_path, f"missing Gherkin keyword: {keyword.strip()}")

    if not re.search(r"`(domain|application|infrastructure)`", content):
        return fail(issue_path, "missing impacted layer value")

    return 0


def main():
    if len(sys.argv) < 2:
        print("Usage: validate_issue_format.py <issue.md> [<issue.md>...]", file=sys.stderr)
        return 2

    status = 0
    for path in sys.argv[1:]:
        status |= validate(path)
    return status


if __name__ == "__main__":
    raise SystemExit(main())
