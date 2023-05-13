package com.github.jabroekens.spotitube.model;

import com.github.jabroekens.spotitube.model.user.User;

public final class Users {

    public static final User DEFAULT = new User("john", "", "John Doe");
    public static final User DEFAULT_CHANGED_NAME = new User(DEFAULT);

    public static final User ALTERNATIVE = new User("jane", "", "Jane Doe");

    static {
        DEFAULT_CHANGED_NAME.setName("John Smith");
    }

}
