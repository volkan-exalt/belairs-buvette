# Orchestrate Group Order Use Case

## Context

The application layer should coordinate group order creation by loading contributors, building contribution balances, invoking the domain, and persisting the resulting order.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads all contributors before creating the group order.
- [ ] Missing contributors produce a clear error.
- [ ] Contribution inputs are mapped to drink and food token balances.
- [ ] A valid group order is persisted.
- [ ] Domain validation errors are preserved.

## Gherkin scenarios

```gherkin
Feature: Group order use case

Scenario: Create a valid group order
  Given Alice and Bob exist with enough tokens
  When a group order request is submitted
  Then the order is created and saved

Scenario: Submit a group order with an unknown contributor
  Given Bob does not exist
  When Alice submits a group order including Bob
  Then the use case reports the missing contributor
```

## Edge cases

- Duplicate contributor inputs should be merged or rejected consistently.
- Empty contribution lists are rejected.
- Persistence failure should not be hidden.

## Validation

- [ ] Run `./gradlew :application:test`.
