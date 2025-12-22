package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class InsertColumnsNotSetException extends RuntimeException {
    public InsertColumnsNotSetException(
        @Nonnull final String table
    ) {
        super("INSERT columns for from '%s' are not set.".formatted(table));
    }
}
