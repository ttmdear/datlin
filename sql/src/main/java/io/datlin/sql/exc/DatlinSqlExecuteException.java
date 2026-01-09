package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DatlinSqlExecuteException extends RuntimeException {

    @Nullable
    private final String sql;

    public DatlinSqlExecuteException(
        @Nonnull final String message,
        @Nonnull final String sql,
        @Nonnull final Exception cause
    ) {
        super(message, cause);
        this.sql = sql;
    }

    public DatlinSqlExecuteException(
        @Nonnull final String message,
        @Nonnull final Exception cause
    ) {
        super(message, cause);
        this.sql = null;
    }

    public DatlinSqlExecuteException(
        @Nonnull final String message,
        @Nonnull final String sql
    ) {
        super(message, null);
        this.sql = sql;
    }

    public DatlinSqlExecuteException(
        @Nonnull final String message
    ) {
        super(message, null);
        this.sql = null;
    }
}
