# Spotitube Research Report

Jens Broekens (1618608)
Teacher: Meron Brouwer

Course: HBO-ICT/OOSE-DEA
Date: June 2nd, 2023
Version: 1

## Abstract
This document investigated the possibility of adding a structured way to check authorizations for a given authenticated user. Available frameworks and community research were identified to create a an application layer-only prototype tested using a system test.

The results were applied to Spotitube by limiting reading, deleting and modifying playlists belonging to a user other than the authenticated user. This proved to be difficult without a framework such as JPA, as the boundary between the service layer (business logic) and the persistence layer fades to prevent race conditions when a user tries to delete or modify a playlist they do not own.

Instead of only comitting a unit of work once business logic allows it to, a less ideal solution was chosen whereby necessary user information is passed to the persistence layer and the persistence layer returns how many playlists belonging to the user were removed.

## Introduction


## Materials and Methods
## Results
## Discussion
## Conclusion
## List of references
