# Use JaCoCo to enforce code coverage

## Context and Problem Statement

Code coverage is an important metric when determining code quality. There are multiple tools available for Java applications which can give insight into the coverage. To ensure code quality of new code stays high, the metric should be enforced (allowing exceptions).

## Considered Options

* JaCoCo
* Built-in IDE coverage viewer

## Decision Outcome

Chosen option: "JaCoCo", because
* It integrates well with build tooling and CI;
* Does not require a specific IDE.
