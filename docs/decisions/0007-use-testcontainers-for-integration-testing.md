# Use Testcontainers for integration testing to ensure similar execution environment

## Context and Problem Statement

When integration testing you ideally want the execution environment for the integration test to be as similar as possible to that of production. Frameworks exists which can emulate production environments, but they have drawbacks.

## Considered Options

* Arquillian + H2
* Starting the application separately in the background
* Unit tests
* Testcontainers

## Decision Outcome

Chosen option: "Testcontainers", because
* It's easy to set up and maintain;
* It's closest to a production environment ("black box").
