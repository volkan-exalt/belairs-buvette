# Send Order Status Notifications

## Context

Festival goers must be notified when an order is acknowledged, ready, or changed after bartender review.

## Impacted layer

`infrastructure`

## Acceptance criteria

- [ ] Acknowledgement notifications include the estimated preparation time.
- [ ] Ready notifications tell the festival goer they can pick up the order.
- [ ] Accepted change notifications include the updated estimate.
- [ ] Rejected change notifications are sent with a clear message.
- [ ] Notifications are testable without an external service.

## Gherkin scenarios

```gherkin
Feature: Order status notifications

Scenario: Notify an acknowledged order
  Given an order is acknowledged with an estimate
  When the notification service sends an update
  Then the festival goer receives the preparation message

Scenario: Reject a notification without an order
  Given no order is provided
  When the notification service is called
  Then the infrastructure service rejects the input
```

## Edge cases

- Message text should remain friendly and explicit.
- Notification timestamps should come from an injectable clock.

## Validation

- [ ] Run `./gradlew :infrastructure:test`.
