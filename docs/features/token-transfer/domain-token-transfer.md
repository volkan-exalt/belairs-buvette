# Model Token Transfer Requests

## Context

Festival goers can transfer tokens to another festival goer. Transfers are limited and must be confirmed by the recipient.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] A transfer request starts pending.
- [ ] A festival goer can transfer up to 3 drink tokens.
- [ ] A festival goer can transfer up to 3 food tokens.
- [ ] The sender balance cannot become negative.
- [ ] Confirming deducts tokens from the sender and adds them to the recipient.
- [ ] Rejecting completes the request without moving tokens.

## Gherkin scenarios

```gherkin
Feature: Token transfer

Scenario: Confirm a valid transfer
  Given Alice has enough tokens and Bob can receive tokens
  When Bob confirms Alice's transfer request
  Then Alice's tokens are deducted
  And Bob's tokens are increased

Scenario: Reject a transfer above the limit
  Given Alice has tokens
  When Alice tries to transfer 4 drink tokens
  Then the domain rejects the transfer request
```

## Edge cases

- Completed transfers cannot be confirmed or rejected again.
- Drink and food transfer limits are checked independently.
- Recipients must be present.

## Validation

- [ ] Run `./gradlew :domain:test`.
