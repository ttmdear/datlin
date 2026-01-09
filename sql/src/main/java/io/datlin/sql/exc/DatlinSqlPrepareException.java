package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DatlinSqlPrepareException extends RuntimeException {

    @Nullable
    private final String sql;

    public DatlinSqlPrepareException(
        @Nonnull final String message
    ) {
        super(message);
        this.sql = null;
    }

    public DatlinSqlPrepareException(
        @Nonnull final String message,
        @Nonnull final String sql
    ) {
        super(message);
        this.sql = sql;
    }
}
