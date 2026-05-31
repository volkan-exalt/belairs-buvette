---
name: create-issue
description: Generate development issue Markdown files from FEATURES.md for the Bel'Air Buvette project. Use when asked to create issues, split features by impacted module or layer, add acceptance criteria, Gherkin scenarios, edge cases, and validation commands under docs/features/.
---

# Create Issue

Use this skill to generate development issues from `FEATURES.md`.

## Workflow

1. Read `FEATURES.md`.
2. Identify impacted layers for each feature: `domain`, `application`, `infrastructure`.
3. Generate one issue per impacted layer when a feature spans multiple layers.
4. Group closely related features only when the group remains clear and testable.
5. Use `templates/issue-template.md` as the output structure.
6. Save issues under `docs/features/<feature-slug>/<issue-slug>.md`.
7. Validate each issue with `scripts/validate_issue_format.py <path>` from the repository root, or with `.github/skills/create-issue/scripts/validate_issue_format.py <path>` from the skill.

## Required Issue Format

Each issue must contain:

- H1 title starting with `# `
- `## Context`
- `## Impacted layer`
- `## Acceptance criteria`
- `## Gherkin scenarios`
- `## Edge cases`
- `## Validation`

Acceptance criteria must be Markdown checkboxes.
Gherkin scenarios must include at least one happy path and one edge case.
Validation must include a Gradle command or explicit documentation-only validation.

## Project Rules

- Domain issues must reference business rules from `FEATURES.md`.
- Application issues must orchestrate use cases without moving business rules out of `domain`.
- Infrastructure issues must cover adapters, persistence, clocks, or notifications.
- Keep titles concise and explicit.
- Use project modules exactly as named: `domain`, `application`, `infrastructure`.

## Extra Resources

- Read `reference.md` when a feature spans several modules or when deciding whether to split issues.
- Read `examples.md` when the expected Markdown or Gherkin style is unclear.
- Use `templates/issue-template.md` for every generated issue.
