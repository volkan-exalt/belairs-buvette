# Model Group Orders With Pooled Tokens

## Context

Groups of festival goers can pool tokens to place one shared order. The pooled contribution must cover the order cost and each contributor must have enough tokens.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] A group order accepts contributions from one or more festival goers.
- [ ] Total contributions must cover the order cost.
- [ ] Each contributor must have enough tokens for their own contribution.
- [ ] Contributions are deducted when the group order is created.
- [ ] Canceling an unacknowledged group order refunds contributions.

## Gherkin scenarios

```gherkin
Feature: Group order

Scenario: Place a group order with sufficient pooled tokens
  Given Alice and Bob have enough tokens together
  When they pool tokens for a shared order
  Then the group order is created
  And each contribution is deducted

Scenario: Reject insufficient pooled tokens
  Given Alice and Bob do not pool enough food tokens
  When they try to place a meal order
  Then the domain rejects the group order
```

## Edge cases

- Empty contribution maps are rejected.
- A contributor cannot contribute more tokens than they own.
- Refunds use the original contribution amounts.

## Validation

- [ ] Run `./gradlew :domain:test`.
