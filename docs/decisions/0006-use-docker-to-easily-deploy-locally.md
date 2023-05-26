# Use Docker to easily deploy locally

## Context and Problem Statement

A classical problem in software development is "It works on my machine". To facilitate integration testing, there should be way to easily deploy the application on your local machine while ensuring the same execution environment.

## Considered Options

* Shell scripts to install/deploy the necessary software.
* Containerizing the application using Docker

## Decision Outcome

Chosen option: "Containerizing the application using Docker", because
* It solves the "It works on my machine" problem;
* Is easy to maintain;
* Makes it easy to deploy.
