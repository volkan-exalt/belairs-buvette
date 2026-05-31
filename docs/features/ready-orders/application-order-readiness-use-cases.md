# Orchestrate Order Readiness Use Cases

## Context

Once an order is fully prepared, the application layer must mark it as ready, persist the updated status, and trigger a ready notification so the festival goer can pick it up.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads an acknowledged order and verifies preparation completeness.
- [ ] The order is marked ready through the domain.
- [ ] The ready status is persisted.
- [ ] A ready notification is triggered through an output port.
- [ ] Orders that are not fully prepared cannot be marked ready.

## Gherkin scenarios

```gherkin
Feature: Order readiness use cases

Scenario: Mark a fully prepared order as ready
  Given an order is acknowledged and all items are prepared
  When the bartender marks the order as ready
  Then the order status becomes ready
  And a ready notification is sent to the festival goer

Scenario: Reject readiness for a partially prepared order
  Given an order is acknowledged but only some items are prepared
  When the bartender marks the order as ready
  Then the use case rejects the action
  And the order status remains acknowledged
```

## Edge cases

- The order is not acknowledged.
- Prepared quantities exceed ordered quantities.
- The order has already been canceled.

## Validation

- [ ] Run `./gradlew :application:test`
