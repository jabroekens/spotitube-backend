package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.user.User;

public final class Users {

    public static User JohnDoe() {
        return new User("john", "$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk", "John Doe");
    }

    public static User JohnSmith() {
        var u = JohnDoe();
        u.setName("John Smith");
        return u;
    }

    public static User JaneDoe() {
        return new User("jane", "$argon2id$v=19$m=16,t=2,p=1$cDFiT1NWNHR6WkNZdUxrcA$NWwrXvOqes96osF+j5il9ahsDhJvnhnzyRhVQUuifJk", "Jane Doe");
    }

}
