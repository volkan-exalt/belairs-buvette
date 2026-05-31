---
agent: codex
name: TDD Red step
description: Write only the failing test for a provided scenario, without production code.
argument-hint: <issue reference or Gherkin scenario>
tools: ['codebase', 'editFiles', 'runCommands']
model: GPT-5
---

# TDD Red Step Prompt

## Instructions

1. Analyze the provided scenario carefully.
   - If the input is an issue reference, retrieve the issue content and extract the requested scenario.
   - If the input is a direct Gherkin scenario, use it as the source of truth.
2. Route the test to the right module.
   - Use `domain` for pure business rules, value objects, pricing, token balances, order statuses, and domain validation.
   - Use `application` for user workflows, orchestration, stock checks, repository coordination, command/query handling, and returned DTOs.
   - Use `infrastructure` for persistence adapters, notifications, clocks, schedulers, and external integrations.
3. Load only the relevant guidelines.
   - Always apply `AGENTS.md` routing rules.
   - For tests, apply `docs/agents/instructions/testing-guidelines.md`.
   - If production Java code is mentioned for context, apply `docs/agents/instructions/coding-guidelines.md`, but do not edit production code.
4. Check whether a test file already exists for the target scope.
   - Append to the existing focused test file when it clearly matches.
   - Otherwise create a new `*Test.java` file in the correct module and package.
5. Write exactly one failing test for the scenario.
   - Preserve the Given/When/Then meaning in the test body.
   - Use JUnit Jupiter only.
   - Do not introduce unknown assertion libraries.
6. Run the smallest relevant Gradle test task and confirm the test is red.

## Critical Requirements

- CRITICAL: Never create or modify production code during this step.
- CRITICAL: Never make the test pass by weakening assertions.
- CRITICAL: The test must fail because the expected behavior is not implemented yet.
- CRITICAL: If the scenario cannot compile without production code, leave the compile failure as the Red result and explain the missing production type.

## Naming Rules

Good test names:

- `shouldCreatePendingOrderWhenAvailableArticleIsOrdered`
- `shouldRejectOrderWhenArticleIsOutOfStock`
- `shouldReturnOrderIdentifierAfterSuccessfulOrderCreation`

Bad test names:

- `test1`
- `orderTest`
- `shouldWork`

## Positive Example

Input:

```gherkin
Scenario: Commande simple avec un article disponible
  Given un festivalier identifié
  And un article "Mojito" disponible en stock
  When le festivalier passe une commande pour 1 "Mojito"
  Then la commande est créée avec le statut "EN_ATTENTE"
  And le festivalier reçoit un identifiant de commande
```

Expected behavior:

- Choose `application` because the scenario describes a user workflow and stock availability.
- Create or update an application test.
- Assert that an order id is returned and that the status is `EN_ATTENTE`.
- Run `./gradlew :application:test`.
- Do not create `PlaceOrderUseCase`, DTOs, repositories, stock adapters, or production status mapping in the Red step.

## Negative Example

Do not do this in Red:

```java
// Bad: production code created during Red step
public class PlaceOrderUseCase {
    public OrderResult placeOrder(...) {
        return new OrderResult("EN_ATTENTE");
    }
}
```

Why it is wrong:

- It implements production behavior before the failing test is confirmed.
- It hides whether the test really captures a missing behavior.

## Iteration Notes

- The prompt was tightened to route stock and end-user workflows to `application`.
- The production-code ban was made explicit because Red must only create tests.
- The examples use JUnit Jupiter and avoid external assertion libraries because this project currently uses JUnit only.
