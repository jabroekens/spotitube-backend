# Use DataSource for database connections

## Context and Problem Statement

Connecting with a database requires knowing where it can be reached and how. When running multiple instances of an application or a database, this information is not constant and thus should not be embedded in the code.

## Considered Options

* Environment variable for connection URI
* DataSource

## Decision Outcome

Chosen option: "DataSource", because
* The application does not need to know anything apart from the JNDI name;
* It has built-in support for connection pooling.

## More Information

* [Why do we use a DataSource instead of a DriverManager?](https://stackoverflow.com/a/15198928)
