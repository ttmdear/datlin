package io.datlin.sql.exception;

import jakarta.annotation.Nonnull;

public class FromNotSetException extends RuntimeException {
    public FromNotSetException() {
        super("From not set");
    }
}
