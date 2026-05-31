---
agent: codex
name: TDD Green step
description: Make one existing Red test pass using TDD as if you meant it, with temporary production code inside the test class only.
argument-hint: <test file path> <test method name>
tools: ['codebase', 'editFiles', 'runCommands']
model: GPT-5
---

# TDD Green Step Prompt

## Input

Provide:

- the path of the test file created during Red
- the exact test method name to make pass

Example:

```text
application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java
shouldCreatePendingOrderWhenAvailableArticleIsOrdered
```

## Instructions

1. Open the specified test file and locate the specified test method.
2. Run the targeted test once if its current failure is unknown.
3. Implement the minimum temporary code required to make that single test pass.
4. Put all temporary implementation code inside the same test class.
5. Prefer private static nested classes, records, enums, or helper methods inside the test class.
6. Do not add behavior that is not required by the target test.
7. Run only the smallest relevant Gradle test command until the target test is green.
8. Return the structured JSON output described below.

## Critical Requirements

- CRITICAL: Do not create or modify production files under `src/main`.
- CRITICAL: Do not modify unrelated test files.
- CRITICAL: Do not modify other test methods in the same class.
- CRITICAL: Do not weaken the target test expectation.
- CRITICAL: Do not add a broad implementation. Implement only what the current test forces.
- CRITICAL: If the test cannot pass without changing production code, stop and explain why in JSON.

## Positive Example

Input:

```text
application/src/test/java/com/example/application/PlaceOrderUseCaseTest.java
shouldCreatePendingOrderWhenAvailableArticleIsOrdered
```

Allowed output shape in the test class:

```java
private record OrderResult(String status, UUID orderId) {
}

private static final class TemporaryPlaceOrderUseCase {
    OrderResult placeOrder() {
        return new OrderResult("EN_ATTENTE", UUID.randomUUID());
    }
}
```

Why it is allowed:

- The implementation is temporary.
- It lives inside the test class.
- It only satisfies the current test.

## Negative Example

Do not create this during Green:

```java
// Bad: production file created too early
package com.example.application;

public class PlaceOrderUseCase {
}
```

Why it is wrong:

- It changes production code during Green.
- It skips the TDD-as-if-you-meant-it constraint.

## Structured Output

Return valid JSON only:

```json
{
  "status": "green",
  "testFile": "application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java",
  "testMethod": "shouldCreatePendingOrderWhenAvailableArticleIsOrdered",
  "productionFilesModified": [],
  "testFilesModified": [
    "application/src/test/java/com/it/exalt/belair/application/PlaceOrderUseCaseTest.java"
  ],
  "temporaryImplementationLocation": "nested classes or records inside the test class",
  "validationCommand": ".\\gradlew.bat :application:test --tests com.it.exalt.belair.application.PlaceOrderUseCaseTest",
  "validationResult": "passed",
  "notesForRefactor": [
    "Move the temporary use case/result types from the test class into production during Refactor."
  ]
}
```

## Iteration Notes

- The production-code ban is explicit because Green must keep implementation inside the test.
- The JSON output is required so the Refactor step can identify temporary code and modified files.
- Negative examples are included to prevent creating `src/main` classes too early.
