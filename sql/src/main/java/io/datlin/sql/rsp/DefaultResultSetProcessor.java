package io.datlin.sql.rsp;

import io.datlin.sql.exc.GettingResultSetValueException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DefaultResultSetProcessor implements ResultSetProcessor {

    public DefaultResultSetProcessor() {

    }

    @Nullable
    @Override
    public <T, R> T getValue(
        @Nonnull final ResultSet resultSet,
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final Class<R> recordClass,
        @Nonnull final String field,
        @Nonnull final Class<T> fieldType
    ) {
        Object value;

        try {
            value = resultSet.getObject(column);
        } catch (SQLException e) {
            throw new GettingResultSetValueException(table, column, e);
        }

        if (value == null) {
            return null;
        }

        if (value instanceof java.sql.Timestamp timestamp && fieldType.equals(LocalDateTime.class)) {
            return (T) timestamp.toLocalDateTime();
        }

        return (T) value;
    }
}

