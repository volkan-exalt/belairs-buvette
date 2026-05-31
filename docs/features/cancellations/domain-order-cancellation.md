# Model Order Cancellation And Refunds

## Context

Festival goers can cancel an order only before it is acknowledged. Tokens used for the order must be refunded.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Created orders can be canceled.
- [ ] Canceling refunds drink and food tokens.
- [ ] Acknowledged orders cannot be canceled.
- [ ] Ready orders cannot be canceled.
- [ ] Canceled orders expose a canceled status.

## Gherkin scenarios

```gherkin
Feature: Order cancellation

Scenario: Cancel a created order
  Given a festival goer placed an unacknowledged order
  When the order is canceled
  Then the order status is canceled
  And the spent tokens are refunded

Scenario: Reject cancellation after acknowledgement
  Given an order has been acknowledged
  When the festival goer tries to cancel it
  Then the domain rejects the cancellation
```

## Edge cases

- Refunds must not be applied twice.
- Group order cancellations refund contributors, not only the representative.
- Cancellation confirmation belongs outside the domain.

## Validation

- [ ] Run `./gradlew :domain:test`.
