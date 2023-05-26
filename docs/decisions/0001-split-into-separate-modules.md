# Split api/implementation into separate modules to lower coupling

## Context and Problem Statement

Reducing coupling by splitting parts of the codebase into separate packages does not prevent (accidentally) calling code of an implementation instead of an api, leading to tight-coupling.

## Considered Options

* Using ArchUnit to enforce package A doesn't call code from package B
* Splitting api/implementation into separate modules

## Decision Outcome

Chosen option: "Splitting api/implementation into separate modules", because
* It scales well and allows replacing an underlying implementation when requirements change.
* Does not require learning a new API.

### Consequences

* Good, because {positive consequence, e.g., improvement of one or more desired qualities, …}
* Bad, because {negative consequence, e.g., compromising one or more desired qualities, …}

## Pros and Cons of the Options

### Using ArchUnit to enforce package A doesn't call code from package B

* Good, because the project structure can stay simple.
* Bad, because it requires learning a new API.

### Splitting api/implementation into separate modules

* Good, because it scales well with build tooling.
* Good, because the depending module can be completely unaware of an implementation.
* Good, because an implementation can be swapped without having to rebuild the application.
