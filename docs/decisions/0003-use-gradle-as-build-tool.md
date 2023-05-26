# Use Gradle as build tool to keep project maintainable

## Context and Problem Statement

Maven is a well known and battle-hardened build tool for Java applications, but its syntax is verbose and as projects get more complex, it gets harder to configure the build tool in a way that facilitates this.

## Considered Options

* Maven
* Gradle

## Decision Outcome

Chosen option: "Gradle", because
* It's very flexible and scales well with complex projects;
* It's less verbose (convention of configuration);
* It integrates well with IDEs;
* It supports multiple languages. (Think Angular frontend as part of the project)

## More Information

* [Gradle vs Maven Comparison](https://gradle.org/maven-vs-gradle/)
