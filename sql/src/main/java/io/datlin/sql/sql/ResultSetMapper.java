package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class ResultSetMapper {

    @Nullable
    public <T> T map(
        @Nonnull final Object value,
        @Nonnull final Class<T> type
    ) {
        if (value == null) {
            return null;
        }

        if (value instanceof java.sql.Timestamp timestamp && type.equals(LocalDateTime.class)) {
            return (T) timestamp.toLocalDateTime();
        }

        return (T) value;
    }
}

