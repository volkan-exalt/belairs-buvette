# Orchestrate Order Cancellation Use Case

## Context

Festival goers can cancel orders before acknowledgement. The application layer must verify the order state, execute domain cancellation and refund logic, persist the cancellation, and trigger a cancellation confirmation notification.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads the order and verifies it is not acknowledged.
- [ ] The domain cancellation flow refunds drink and food tokens.
- [ ] The canceled status is persisted.
- [ ] A cancellation confirmation notification is triggered through an output port.
- [ ] Cancellation is rejected for acknowledged, ready, or already canceled orders.

## Gherkin scenarios

```gherkin
Feature: Order cancellation use case

Scenario: Cancel a created order
  Given a festival goer placed an unacknowledged order
  When the festival goer cancels the order
  Then the order status becomes canceled
  And tokens are refunded
  And a cancellation confirmation notification is sent

Scenario: Reject cancellation after acknowledgement
  Given an order has been acknowledged
  When the festival goer tries to cancel it
  Then the use case rejects the cancellation
  And the order remains acknowledged
```

## Edge cases

- The order identifier does not exist.
- Refund is not applied twice.
- The order is already ready or canceled.

## Validation

- [ ] Run `./gradlew :application:test`
