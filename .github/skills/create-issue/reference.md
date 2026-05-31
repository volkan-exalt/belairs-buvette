# Create Issue Reference

Use this reference for advanced decisions when generating issues from `FEATURES.md`.

## Splitting Rules

- Generate one issue per impacted module when work crosses module boundaries.
- Use `domain` for business invariants, value objects, entities, pricing, token rules, order state transitions, and preparation policies.
- Use `application` for use cases that orchestrate repositories, domain objects, and outputs.
- Use `infrastructure` for persistence adapters, notification gateways, clocks, schedulers, and external integrations.

## Grouping Rules

- Group features only when they belong to the same user workflow and the same layer.
- Do not group unrelated acceptance criteria just to reduce file count.
- Prefer several small issues over one issue that mixes domain, application, and infrastructure tasks.

## Gherkin Rules

- Include at least two scenarios.
- Include one happy path and one edge case or rejection path.
- Use `Given`, `When`, `Then`, and optionally `And`.
- Keep scenarios behavioral, not implementation-specific.

## Validation Rules

- Every issue must include a validation command.
- Domain issues validate with `./gradlew :domain:test`.
- Application issues validate with `./gradlew :application:test`.
- Infrastructure issues validate with `./gradlew :infrastructure:test`.
- Full project validation can use `./gradlew test`.
