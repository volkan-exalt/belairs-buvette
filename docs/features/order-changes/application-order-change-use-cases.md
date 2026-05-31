# Orchestrate Order Change Use Cases

## Context

The application layer should handle change requests by loading orders, applying direct changes for created orders, notifying bartenders for acknowledged orders, and saving updates.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads the target order.
- [ ] Created orders are changed directly through the domain.
- [ ] Acknowledged orders create a reviewable change request.
- [ ] Accepted or rejected decisions are persisted.
- [ ] Relevant notifications are triggered through ports.

## Gherkin scenarios

```gherkin
Feature: Order change use cases

Scenario: Change a created order
  Given an order is still created
  When a festival goer submits new items
  Then the order is updated and saved

Scenario: Request change for an acknowledged order
  Given an order is acknowledged
  When a festival goer submits new items
  Then a bartender review request is created
```

## Edge cases

- Unknown order identifiers are reported clearly.
- Invalid proposed items are rejected.
- Notification failure should be visible to the caller or logs.

## Validation

- [ ] Run `./gradlew :application:test`.
