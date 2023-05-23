package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class JdbcHelper {

    private JdbcHelper() {
        throw new UnsupportedOperationException("Cannot instantiate util class");
    }

    public static PreparedStatement withParams(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            var obj = params[i];
            stmt.setObject(i + 1, obj);
        }
        return stmt;
    }

    public static PreparedStatement withBatchParams(PreparedStatement stmt, Object[][] batchParams)
      throws SQLException {
        for (var params : batchParams) {
            withParams(stmt, params);
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
            var fieldsToSet = new HashMap<Field, Object>();

            for (var field : getAllFields(resultType)) {
                var fieldType = field.getType();
                field.setAccessible(true);

                var value = getValueForField(resultType, resultSet, field);
                if (value != null) {
                    fieldsToSet.put(field, value);
                } else if (fieldType.getAnnotation(Entity.class) != null && isRegularClass(fieldType)) {
                    fieldsToSet.put(field, toEntity(fieldType, resultSet));
                }
            }

            return constructEntity(resultType, fieldsToSet);
        } catch (
          InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e
        ) {
            throw new PersistenceException(e);
        }
    }

    private static <T> T constructEntity(Class<T> resultType, HashMap<Field, Object> fieldsToSet)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (fieldsToSet.values().stream().anyMatch(Objects::nonNull)) {
            var constructor = resultType.getDeclaredConstructor();
            constructor.setAccessible(true);
            var result = constructor.newInstance();

            for (var entry : fieldsToSet.entrySet()) {
                var field = entry.getKey();
                var value = entry.getValue();
                field.set(result, value);
            }

            return result;
        }

        return null;
    }

    private static List<Field> getAllFields(Class<?> resultType) {
        var fields = new ArrayList<Field>();
        Collections.addAll(fields, resultType.getDeclaredFields());

        if (resultType.getSuperclass() != null) {
            fields.addAll(getAllFields(resultType.getSuperclass()));
        }

        return fields;
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

    private static <T> boolean isRegularClass(Class<T> type) {
        return !(type.isArray() || Modifier.isAbstract(type.getModifiers()) || type.isRecord());
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
