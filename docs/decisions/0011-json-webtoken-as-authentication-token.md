# Use JSON Web Tokens as authentication token to validate authorizations

## Context and Problem Statement

Once a client has logged in, it receives an authentication token which is used in subsequent requests.
In a simple implementation, the token is just a means of communicating that a request is coming from an authenticated user.
This means that authorizations still need to be looked up before performing any 'protected' action.
As a system grows in complexity it is worthwhile to not have to repeatedly look up which authorizations a user has when processing a request and instead utilize information embedded in the token for this.
This is only possible if the token can be absolutely trusted, that is to say: it can be ascertained that the token has not been modified by the user to include ungranted authorizations.

## Considered Options

* Keep it simple: persist a randomely generated token with authorization information.
* Use JSON Web Tokens

## Decision Outcome

Chosen option: "Use JSON Web Tokens", because
* It does not require having to look up authorizations on every request;
* It does not require to be persisted as all the information is contained in the token.

## More Information

* [How to implement REST token-based authentication with JAX-RS and Jersey](https://stackoverflow.com/a/26778123)
* [Introduction to JSON Web Tokens](https://jwt.io/introduction)
