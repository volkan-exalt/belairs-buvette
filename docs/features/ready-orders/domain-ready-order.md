# Model Ready Order Rules

## Context

Bartenders can mark an order as ready only when enough prepared items exist to fulfill it.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Prepared items can be registered only for acknowledged orders.
- [ ] Prepared quantities must be positive.
- [ ] An order can be marked ready only when all lines are prepared.
- [ ] Marking ready changes the order status to ready.
- [ ] Preparing an item not present in the order is rejected.

## Gherkin scenarios

```gherkin
Feature: Ready order

Scenario: Mark a fully prepared order as ready
  Given an acknowledged order has all required items prepared
  When the bartender marks the order as ready
  Then the order status is ready

Scenario: Reject readiness with missing prepared items
  Given an acknowledged order is only partially prepared
  When the bartender marks the order as ready
  Then the domain rejects the action
```

## Edge cases

- Over-preparing an item should not require more than the ordered quantity.
- Ready orders cannot be prepared through invalid status transitions.

## Validation

- [ ] Run `./gradlew :domain:test`.
