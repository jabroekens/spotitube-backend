package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JdbcHelper {

    private JdbcHelper() {
        throw new UnsupportedOperationException("Cannot instantiate util class");
    }

    public static PreparedStatement withParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    public static PreparedStatement withBatchParams(PreparedStatement stmt, Object[][] batchParams)
      throws SQLException {
        for (int i = 0; i < batchParams.length; i++) {
            var params = batchParams[i];

            for (int j = 0; j < params.length; j++) {
                stmt.setObject((i * 2) + j + 1, params[j]);
            }
            stmt.addBatch();
        }
        return stmt;
    }

    /**
     * Returns an instance of {@link T} whose primitive and {@link Entity @Entity} fields are extracted from {@code resultSet}.
     * It assumes columns in {@code resultSet} have been aliased to {@code <DomainTypeSimpleName>_<fieldName>}.
     */
    public static <T> T toEntity(Class<T> resultType, ResultSet resultSet) {
        try {
            var constructor = resultType.getDeclaredConstructor();
            constructor.setAccessible(true);
            var result = constructor.newInstance();

            for (var field : resultType.getDeclaredFields()) {
                var fieldType = field.getType();
                field.setAccessible(true);

                var value = getValueForField(resultType, resultSet, field);
                if (value != null) {
                    field.set(result, value);
                } else if (fieldType.getAnnotation(Entity.class) != null) {
                    // XXX ^ might break when field is array/interface/record etc.
                    field.set(result, toEntity(fieldType, resultSet));
                }
            }

            return result;
        } catch (
          InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e
        ) {
            throw new PersistenceException(e);
        }
    }

    private static <T> Object getValueForField(Class<T> resultType, ResultSet resultSet, Field field) {
        try {
            var fieldType = field.getType();
            var columnName = resultType.getSimpleName() + "_" + field.getName();
            return resultSet.getObject(columnName, wrapIfPrimitive(fieldType));
        } catch (SQLException ignored) {
            return null;
        }
    }

    private static Class<?> wrapIfPrimitive(Class<?> type) {
        if (type == Boolean.TYPE) return Boolean.class;
        if (type == Character.TYPE) return Character.class;
        if (type == Byte.TYPE) return Byte.class;
        if (type == Short.TYPE) return Short.class;
        if (type == Integer.TYPE) return Integer.class;
        if (type == Long.TYPE) return Long.class;
        if (type == Float.TYPE) return Float.class;
        if (type == Double.TYPE) return Double.class;
        if (type == Void.TYPE) return Void.class;
        return type;
    }

}
