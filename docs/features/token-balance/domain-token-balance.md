# Model Festival Goer Token Balances

## Context

Festival goers need to consult the remaining balance of drink and food tokens. The domain must protect token invariants before any application or infrastructure layer can expose them.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] A festival goer has separate drink and food token balances.
- [ ] Token balances can be zero or positive.
- [ ] Negative token balances are rejected.
- [ ] A daily allocation can represent 6 drink tokens and 9 food tokens.
- [ ] Unspent tokens are not carried over when a new daily balance is issued.

## Gherkin scenarios

```gherkin
Feature: Token balance

Scenario: Consult a valid token balance
  Given a festival goer with 6 drink tokens and 9 food tokens
  When the balance is consulted
  Then the drink balance is 6
  And the food balance is 9

Scenario: Reject a negative balance
  Given a festival goer token balance
  When a negative drink or food token amount is created
  Then the domain rejects the balance
```

## Edge cases

- Zero drink tokens and zero food tokens are valid.
- Subtracting more tokens than available is rejected.
- A new festival day resets the balance instead of carrying leftovers.

## Validation

- [ ] Run `./gradlew :domain:test`.
