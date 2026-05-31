# Model Single And Multi Item Orders

## Context

A festival goer can order one or more drinks or food items in a single order. The total cost must not exceed either token balance.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] An order can contain multiple order lines.
- [ ] Total drink and food costs are calculated independently.
- [ ] The order is rejected when either token balance is insufficient.
- [ ] Placing an order deducts the required tokens.
- [ ] Empty orders are rejected.

## Gherkin scenarios

```gherkin
Feature: Multi item order

Scenario: Place an order with several items
  Given a festival goer has enough drink and food tokens
  When they order a beer, a snack, and a meal
  Then the order is created
  And the corresponding tokens are deducted

Scenario: Reject an order that exceeds food tokens
  Given a festival goer has only 1 food token
  When they order a meal costing 3 food tokens
  Then the domain rejects the order
```

## Edge cases

- Drink and food balances are checked separately.
- Duplicate item types can be represented through quantities.
- An order must contain at least one line.

## Validation

- [ ] Run `./gradlew :domain:test`.
