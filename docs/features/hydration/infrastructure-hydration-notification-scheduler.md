# Schedule Hydration Reminder Notifications

## Context

Infrastructure must send friendly hydration reminders to festival goers according to the domain policy and observable alcohol consumption data.

## Impacted layer

`infrastructure`

## Acceptance criteria

- [ ] The scheduler sends reminders only inside the allowed time window.
- [ ] The scheduler sends reminders every hour by default.
- [ ] The scheduler sends reminders every 30 minutes for festival goers above the alcohol threshold.
- [ ] Notifications include a friendly responsible drinking message.
- [ ] Time is controlled through an injectable clock.

## Gherkin scenarios

```gherkin
Feature: Hydration notification scheduler

Scenario: Send a due hydration reminder
  Given the time is within the reminder window
  And Alice has no recent reminder
  When the scheduler runs
  Then Alice receives a hydration notification

Scenario: Skip reminders outside the window
  Given the time is before 11:00
  When the scheduler runs
  Then no hydration notification is sent
```

## Edge cases

- Missing consumption data defaults to regular reminder frequency.
- Running the scheduler twice immediately should not duplicate notifications.
- Multiple festival goers are evaluated independently.

## Validation

- [ ] Run `./gradlew :infrastructure:test`.
