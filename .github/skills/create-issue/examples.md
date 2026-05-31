# Create Issue Examples

## Good Example

```markdown
# Model Token Transfer Requests

## Context

Festival goers can transfer tokens to another festival goer. Transfers are limited and must be confirmed by the recipient.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] A transfer request starts pending.
- [ ] A festival goer can transfer up to 3 drink tokens.

## Gherkin scenarios

```gherkin
Feature: Token transfer

Scenario: Confirm a valid transfer
  Given Alice has enough tokens
  When Bob confirms Alice's transfer request
  Then Alice's tokens are deducted

Scenario: Reject a transfer above the limit
  Given Alice has tokens
  When Alice tries to transfer 4 drink tokens
  Then the domain rejects the transfer request
```

## Edge cases

- Completed transfers cannot be confirmed twice.

## Validation

- [ ] Run `./gradlew :domain:test`.
```

## Bad Example

```markdown
# Token stuff

Do the token feature.
```

Why it is bad:

- No layer is identified.
- No acceptance criteria are listed.
- No Gherkin scenarios cover happy path and edge cases.
- No validation command is provided.
