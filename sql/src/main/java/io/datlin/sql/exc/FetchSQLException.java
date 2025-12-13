package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class FetchSQLException extends RuntimeException {
    public FetchSQLException(
        @Nonnull final String sql,
        @Nonnull final Exception cause
    ) {
        super("Error during fetching: " + sql, cause);
    }

}
