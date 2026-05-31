# Expose Token Balance Query Use Case

## Context

The backend needs a use case for consulting a festival goer's remaining token balance without leaking repository details to callers.

## Impacted layer

`application`

## Acceptance criteria

- [ ] A query use case returns drink and food token balances for an existing festival goer.
- [ ] The use case reports a clear error when the festival goer does not exist.
- [ ] The use case does not modify balances.
- [ ] Business rules remain in `domain`.

## Gherkin scenarios

```gherkin
Feature: Token balance query

Scenario: Query an existing festival goer balance
  Given Alice exists with 6 drink tokens and 9 food tokens
  When her token balance is requested
  Then the response contains 6 drink tokens and 9 food tokens

Scenario: Query an unknown festival goer
  Given no festival goer named Alice exists
  When Alice's token balance is requested
  Then the use case reports that the festival goer was not found
```

## Edge cases

- Names with leading or trailing spaces should resolve consistently.
- A balance of zero tokens must be returned as a valid result.

## Validation

- [ ] Run `./gradlew :application:test`.
