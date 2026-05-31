# Model Order Acknowledgement And Preparation Estimates

## Context

Bartenders acknowledge orders so festival goers know preparation has started. The domain must calculate estimated readiness based on item types.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Created orders can be acknowledged once.
- [ ] Acknowledgement changes the status to acknowledged.
- [ ] Non-alcoholic drinks add 1 minute per drink type.
- [ ] Normal alcoholic drinks add 2 minutes per drink type.
- [ ] Premium alcoholic drinks add 3 minutes per drink type.
- [ ] Snacks add 2 minutes per snack type.
- [ ] Meals add 10 minutes per meal type plus the longest drink preparation time when drinks are present.

## Gherkin scenarios

```gherkin
Feature: Order acknowledgement

Scenario: Acknowledge a mixed order
  Given an order contains drinks, snacks, and a meal
  When the bartender acknowledges the order
  Then the order status is acknowledged
  And the estimated preparation time is calculated

Scenario: Reject acknowledging an order twice
  Given an order is already acknowledged
  When the bartender acknowledges it again
  Then the domain rejects the action
```

## Edge cases

- Orders with the same item type are prepared together.
- Empty item lists estimate to zero only at estimator level, not valid order creation.
- Meal and drink preparation can overlap according to the feature rule.

## Validation

- [ ] Run `./gradlew :domain:test`.
