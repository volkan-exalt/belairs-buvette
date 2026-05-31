# Orchestrate Order Placement Use Cases

## Context

The application layer should expose use cases for placing drink, food, and multi-item orders while delegating pricing and validation to the domain.

## Impacted layer

`application`

## Acceptance criteria

- [ ] A use case loads the festival goer by identifier or name.
- [ ] A use case converts input items into domain order lines.
- [ ] Successful orders are saved through an order port.
- [ ] Insufficient token errors from the domain are propagated clearly.
- [ ] Business pricing remains in `domain`.

## Gherkin scenarios

```gherkin
Feature: Order placement use case

Scenario: Place a valid multi-item order
  Given Alice exists with enough tokens
  When Alice submits a drink and food order
  Then an order is created and persisted

Scenario: Place an order for an unknown festival goer
  Given Alice does not exist
  When an order is submitted for Alice
  Then the use case reports that Alice was not found
```

## Edge cases

- Unknown item categories are rejected before creating domain objects.
- Empty item lists are rejected.
- Repository failures are surfaced without corrupting domain state.

## Validation

- [ ] Run `./gradlew :application:test`.
