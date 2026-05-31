# Orchestrate Token Transfer Use Case

## Context

The application layer should create, confirm, and reject token transfer requests while keeping transfer rules in the domain.

## Impacted layer

`application`

## Acceptance criteria

- [ ] The use case loads sender and recipient.
- [ ] A transfer request is created through the domain.
- [ ] Recipient confirmation applies the transfer.
- [ ] Recipient rejection completes the request without moving tokens.
- [ ] Missing participants are reported clearly.

## Gherkin scenarios

```gherkin
Feature: Token transfer use case

Scenario: Confirm a transfer through the application
  Given Alice and Bob exist
  When Alice requests a transfer and Bob confirms it
  Then both balances are updated and saved

Scenario: Transfer to an unknown recipient
  Given Bob does not exist
  When Alice requests a token transfer to Bob
  Then the use case reports that Bob was not found
```

## Edge cases

- Duplicate confirmation requests are rejected.
- Persistence failure after confirmation must be handled consistently.

## Validation

- [ ] Run `./gradlew :application:test`.
