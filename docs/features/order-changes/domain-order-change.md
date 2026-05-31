# Model Order Change Rules

## Context

Festival goers can change an order before it is acknowledged. For acknowledged orders, a change request must be reviewed by the bartender.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Created orders can have items changed.
- [ ] Acknowledged orders cannot be directly changed.
- [ ] A change request can be created for an acknowledged order.
- [ ] Accepted changes require at least one transferable prepared item.
- [ ] Accepted changes update the estimated preparation time.

## Gherkin scenarios

```gherkin
Feature: Order change

Scenario: Change an unacknowledged order
  Given an order has not been acknowledged
  When the festival goer replaces its items
  Then the order contains the new items

Scenario: Reject an acknowledged change without transferable items
  Given an acknowledged order has no prepared transferable item
  When a bartender approves the change request
  Then the domain rejects the approval
```

## Edge cases

- Empty proposed item lists are rejected.
- Completed change requests cannot be approved or rejected again.
- The modified order must still respect token rules.

## Validation

- [ ] Run `./gradlew :domain:test`.
