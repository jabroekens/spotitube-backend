package com.github.jabroekens.spotitube.service.api.user;

public class IncorrectPasswordException extends IllegalArgumentException {

    private static final String ERROR_MSG = "Incorrect password given for user with ID '%s'.";

    private final String userId;

    public IncorrectPasswordException(String userId) {
        this(userId, null);
    }

    public IncorrectPasswordException(String userId, Throwable cause) {
        super(ERROR_MSG.formatted(userId));
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

}
