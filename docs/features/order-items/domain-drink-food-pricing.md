# Model Drink And Food Pricing Rules

## Context

Festival goers can order drinks and food. The domain must calculate token costs for non-alcoholic drinks, normal alcoholic drinks, premium alcoholic drinks, snacks, and meals.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Non-alcoholic drinks cost 0 drink tokens.
- [ ] Normal alcoholic drinks cost 1 drink token.
- [ ] Premium alcoholic drinks cost 2 drink tokens.
- [ ] Snacks cost 1 food token.
- [ ] Meals cost 3 food tokens.
- [ ] Invalid item names or quantities are rejected.

## Gherkin scenarios

```gherkin
Feature: Item pricing

Scenario: Calculate drink and food costs
  Given a normal alcoholic drink and a meal
  When the costs are calculated
  Then the drink costs 1 drink token
  And the meal costs 3 food tokens

Scenario: Reject an invalid order line
  Given an order item
  When the quantity is zero
  Then the domain rejects the order line
```

## Edge cases

- Non-alcoholic drinks are valid even though they cost no tokens.
- Blank item names are rejected.
- Costs are multiplied by quantity.

## Validation

- [ ] Run `./gradlew :domain:test`.
