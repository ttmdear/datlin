package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class InsertValuesNotSetException extends RuntimeException {
    public InsertValuesNotSetException(
        @Nonnull final String table
    ) {
        super("INSERT values for from '%s' are not set.".formatted(table));
    }
}
