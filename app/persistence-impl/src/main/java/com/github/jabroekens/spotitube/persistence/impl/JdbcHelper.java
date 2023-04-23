package com.github.jabroekens.spotitube.persistence.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class JdbcHelper {

    private JdbcHelper() {
        throw new UnsupportedOperationException("Cannot instantiate util class");
    }

    public static PreparedStatement withParams(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
        return statement;
    }

}
