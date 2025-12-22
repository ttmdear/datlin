package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class UpdateSetIsNotSetException extends RuntimeException {
    public UpdateSetIsNotSetException(@Nonnull final String table) {
        super("Update from '%s' has no sets".formatted(table));
    }
}
