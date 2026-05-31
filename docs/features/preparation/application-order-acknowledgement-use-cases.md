# Orchestrate Order Acknowledgement Use Cases

## Context

Bartenders acknowledge orders so festival goers know preparation has started. The application layer must load the order, delegate acknowledgement to the domain, persist the updated status and estimate, and trigger an acknowledgement notification.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads the order by identifier.
- [ ] The use case delegates acknowledgement and estimate calculation to the domain.
- [ ] The acknowledged order status and preparation estimate are persisted.
- [ ] An acknowledgement notification is triggered through an output port.
- [ ] Acknowledgement is rejected for already acknowledged or invalid orders.

## Gherkin scenarios

```gherkin
Feature: Order acknowledgement use cases

Scenario: Acknowledge a created order
  Given an order is created and not yet acknowledged
  When the bartender acknowledges the order
  Then the order is saved as acknowledged
  And the preparation estimate is persisted
  And a notification is sent to the festival goer

Scenario: Reject acknowledging an already acknowledged order
  Given an order is already acknowledged
  When the bartender acknowledges it again
  Then the use case rejects the action
  And the order state is unchanged
```

## Edge cases

- The order identifier does not exist.
- The order is already ready or canceled.
- Notification delivery fails after the domain acknowledges the order.

## Validation

- [ ] Run `./gradlew :application:test`
