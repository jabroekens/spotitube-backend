# Use Argon2(id) for password hashing to improve security

## Context and Problem Statement

There are many hashing algorithms available, however not all of them are suited for hashing sensitive information such as passwords.

## Considered Options

* SHA-family
* PBKDF2
* Argon2(id)

## Decision Outcome

Chosen option: "Argon2(id)", because
* It's been around for a while and is recommended (see below);
* It's supported by [Spring Security Crypto Module](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/argon2/Argon2PasswordEncoder.html).

## More Information

* [In 2018, what is the recommended hash to store passwords: bcrypt, scrypt, Argon2?](https://security.stackexchange.com/a/197550)
* [How to securely hash passwords?](https://security.stackexchange.com/a/31846)
