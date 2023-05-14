package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.user.User;

public final class Users {

    public static final User JOHN_DOE = new User("john", "", "John Doe");
    public static final User JOHN_SMITH = new User(JOHN_DOE);
    public static final User JANE_DOE = new User("jane", "", "Jane Doe");

    static {
        JOHN_SMITH.setName("John Smith");
    }

}
