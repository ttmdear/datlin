package io.datlin.sql.rsp;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.ResultSet;

public interface ResultSetProcessor {
    @Nullable
    <T, R> T getValue(
        @Nonnull final ResultSet resultSet,
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final Class<R> recordClass,
        @Nonnull final String field,
        @Nonnull final Class<T> fieldType
    );
}

