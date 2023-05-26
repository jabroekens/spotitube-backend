# Use Jakarta EE to stay up-to-date with bugfixes and new features

## Context and Problem Statement

The assignment lists the use of Java EE as a requirement, but Java EE has been superseded by Jakarta EE and newer application servers have started dropping support for Java EE.

## Decision Drivers

* Work on Java EE has halted, meaning bugfixes and new features will not be backported.
* As new versions of application servers release, the support for Java EE will be dropped.
* Porting a Java EE application to Jakarta EE will become more difficult as newer versions of Jakarta EE release.

## Considered Options

* Stick with Java EE
* Use Jakarta EE

## Decision Outcome

Chosen option: "Use Jakarta EE", because
* This will be a new application and there's no hard requirement for supporting legacy execution environments.
* It's easier to update the application to include bugfixes and take advantage of new features.
