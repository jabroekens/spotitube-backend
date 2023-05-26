# Implement generic NotFound/AlreadyExists exceptions instead of domain-type-specific ones

## Context and Problem Statement

The application layer might need to respond differently depending on which domain entity was not found/already exists. The service layer throws an exception in such a case, which is mapped to a response by the application layer.

## Considered Options

* Type-specific exceptions
* Generic exceptions from which the type can be determined

## Decision Outcome

Chosen option: "Generic exceptions from which the type can be determined", because
* There is no situation where such an exception needs to be caught for one domain type but not the other.

## More Information

* [Is it wise to use generics for exceptions?](https://stackoverflow.com/a/6818317)
