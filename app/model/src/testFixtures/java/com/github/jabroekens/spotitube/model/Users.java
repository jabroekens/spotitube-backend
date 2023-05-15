package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.user.User;

public final class Users {

    public static User JohnDoe() {
        return new User("john", "", "John Doe");
    }

    public static User JohnSmith() {
        var u = JohnDoe();
        u.setName("John Smith");
        return u;
    }

    public static User JaneDoe() {
        return new User("jane", "", "Jane Doe");
    }

}
