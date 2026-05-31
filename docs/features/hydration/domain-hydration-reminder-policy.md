# Model Hydration Reminder Policy

## Context

Festival goers should receive water reminders during hot festival hours. The domain policy decides reminder intervals and allowed notification windows.

## Impacted layer

`domain`

## Acceptance criteria

- [ ] Regular reminders are due every hour.
- [ ] Festival goers with more than 3 alcoholic drinks in the past hour use a 30 minute interval.
- [ ] Reminders are allowed from 11:00 inclusive to 19:00 exclusive.
- [ ] Reminders before 11:00 are scheduled at 11:00.
- [ ] Reminders after 19:00 are scheduled for the next day at 11:00.

## Gherkin scenarios

```gherkin
Feature: Hydration reminder policy

Scenario: Compute a regular reminder interval
  Given the current time is 13:00
  And the festival goer drank 2 alcoholic drinks in the past hour
  When the next reminder interval is calculated
  Then the interval is 1 hour

Scenario: Compute a frequent reminder interval
  Given the current time is 13:00
  And the festival goer drank 4 alcoholic drinks in the past hour
  When the next reminder interval is calculated
  Then the interval is 30 minutes
```

## Edge cases

- 19:00 is outside the reminder window.
- Null time input is rejected.
- Exactly 3 alcoholic drinks still uses the regular hourly interval.

## Validation

- [ ] Run `./gradlew :domain:test`.
