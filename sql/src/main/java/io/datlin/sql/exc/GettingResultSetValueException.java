package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class GettingResultSetValueException extends RuntimeException {
    public GettingResultSetValueException(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final Exception cause
    ) {
        super("Error during getting '%s' reference from result set for `%s`".formatted(table, column), cause);
    }

}
