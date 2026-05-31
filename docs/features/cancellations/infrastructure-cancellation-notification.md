# Send Cancellation Confirmation Notifications

## Context

When an order is canceled, the festival goer must receive a confirmation. Infrastructure owns the notification adapter.

## Impacted layer

`infrastructure`

## Acceptance criteria

- [ ] A notification gateway can send cancellation confirmations.
- [ ] The notification includes a clear cancellation message.
- [ ] Sent notifications can be observed in tests.
- [ ] Notification code does not perform domain validation.

## Gherkin scenarios

```gherkin
Feature: Cancellation notification

Scenario: Notify a canceled order
  Given a festival goer canceled an order
  When the notification service is called
  Then a cancellation confirmation is sent

Scenario: Reject missing notification recipient
  Given no festival goer recipient is provided
  When a notification is created
  Then the infrastructure adapter rejects it
```

## Edge cases

- Blank notification messages are rejected.
- The notification timestamp should be testable with a fixed clock.

## Validation

- [ ] Run `./gradlew :infrastructure:test`.
