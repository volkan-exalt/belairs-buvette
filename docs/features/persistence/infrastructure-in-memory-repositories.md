# Provide In Memory Persistence Adapters

## Context

The infrastructure layer needs adapters to store and retrieve festival goers and orders for development and tests.

## Impacted layer

`infrastructure`

## Acceptance criteria

- [ ] Festival goers can be saved, found by name, listed, and deleted.
- [ ] Orders can be saved, found by id, listed, filtered by status, and deleted.
- [ ] Repository lookups are deterministic for tests.
- [ ] Invalid repository inputs are rejected.
- [ ] Infrastructure depends on domain, not the reverse.

## Gherkin scenarios

```gherkin
Feature: In memory persistence

Scenario: Save and find a festival goer
  Given a festival goer repository
  When Alice is saved
  Then Alice can be found by name

Scenario: Find an unknown order
  Given an order repository
  When an unknown order id is requested
  Then no order is returned
```

## Edge cases

- Festival goer name lookup should normalize casing and surrounding spaces.
- Null identifiers are rejected.
- Listing methods return deterministic ordering.

## Validation

- [ ] Run `./gradlew :infrastructure:test`.
