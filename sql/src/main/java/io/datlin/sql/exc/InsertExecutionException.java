package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class InsertExecutionException extends RuntimeException {
    public InsertExecutionException(
        @Nonnull final String sql,
        @Nonnull final Exception cause
    ) {
        super("Error during executing insert: " + sql, cause);
    }

}
