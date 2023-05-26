# Use Jakarta Bean Validation to validate arguments

## Context and Problem Statement

To ensure the information passed to a method is valid, validations are run. This can be done manually or through a framework.

## Considered Options

* Manually
* Jakarta Bean Validation

## Decision Outcome

Chosen option: "Jakarta Bean Validation", because
* It allows the developer to declaratively validate arguments, making code more readable;
* It integrates well with the Jakarta EE ecosystem.
