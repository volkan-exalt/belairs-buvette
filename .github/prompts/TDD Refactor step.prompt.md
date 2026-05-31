---
agent: codex
name: TDD Refactor step
description: Extract temporary Green-step code from a test class into production through validated micro-steps while preserving behavior.
argument-hint: <JSON output from TDD Green step>
tools: ['codebase', 'editFiles', 'runCommands']
model: GPT-5
---

# TDD Refactor Step Prompt

## Input

Provide the exact JSON output from `/TDD Green step`.

Example:

```json
{
  "status": "green",
  "testFile": "application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java",
  "testMethod": "shouldCreatePendingOrderWhenAvailableArticleIsOrdered",
  "productionFilesModified": [],
  "testFilesModified": [
    "application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java"
  ],
  "temporaryImplementationLocation": "TemporaryPlaceOrderUseCase and OrderResult inside PlaceOrderUseCaseTest",
  "validationCommand": ".\\gradlew.bat :application:test --tests com.it.exalt.belair.application.PlaceOrderUseCaseTest",
  "validationResult": "passed",
  "notesForRefactor": [
    "Move OrderResult and PlaceOrderUseCase behavior from the test class into production during Refactor."
  ]
}
```

## Instructions

1. Read `AGENTS.md`, `docs/agents/instructions/coding-guidelines.md`, and `docs/agents/instructions/testing-guidelines.md`.
2. Open the `testFile` from the JSON and locate temporary implementation code.
3. Move temporary production code into the correct `src/main` package.
4. Proceed by micro-steps:
   - Make exactly one production extraction or cleanup.
   - Update only the target test if needed to point to extracted production code.
   - Run the provided `validationCommand`.
   - Continue only if the test remains green.
5. Keep behavior unchanged. Refactor means move and clean, not expand.
6. After the target test is green, run the smallest broader relevant suite if reasonable.
7. Return the structured JSON output described below.

## Critical Requirements

- CRITICAL: Stop immediately if a test goes red after a micro-step.
- CRITICAL: Do not add functionality not forced by the existing green test.
- CRITICAL: Do not create interfaces, ports, repositories, stock systems, controllers, or abstractions unless the current test forces them.
- CRITICAL: Do not modify unrelated tests.
- CRITICAL: Do not change the scenario assertions to make refactor easier.
- CRITICAL: Preserve module boundaries: application orchestration belongs in `application`, business invariants stay in `domain`, adapters stay in `infrastructure`.

## Positive Example

Input temporary code in a test:

```java
private record OrderResult(String status, UUID orderId) {
}
```

Allowed refactor:

- Create `application/src/main/java/.../OrderResult.java`.
- Remove the nested record from the test.
- Run the target test.

## Negative Example

Do not do this:

```java
public interface OrderRepository {
}

public class StockService {
}
```

Why it is wrong:

- The current test does not force repository or stock abstractions.
- This is over-engineering during Refactor.

## Structured Output

Return valid JSON only:

```json
{
  "status": "refactored",
  "productionFilesModified": [
    "application/src/main/java/com/it/exalt/belair/application/OrderResult.java",
    "application/src/main/java/com/it/exalt/belair/application/PlaceOrderUseCase.java"
  ],
  "testFilesModified": [
    "application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java"
  ],
  "microSteps": [
    {
      "change": "Extract OrderResult record to production",
      "validationCommand": ".\\gradlew.bat :application:test --tests com.it.exalt.belair.application.PlaceOrderUseCaseTest",
      "validationResult": "passed"
    }
  ],
  "broaderValidation": {
    "validationCommand": ".\\gradlew.bat :application:test",
    "validationResult": "passed"
  },
  "remainingNotes": []
}
```

## Iteration Notes

- The prompt explicitly bans abstractions not forced by the test.
- The prompt requires one extraction at a time followed by tests.
- JSON output is constrained so the next workflow step can consume changed files and validation results.
