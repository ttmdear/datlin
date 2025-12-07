package io.datlin.sql.exc;

public class FromNotSetException extends RuntimeException {
    public FromNotSetException() {
        super("From not set");
    }
}
