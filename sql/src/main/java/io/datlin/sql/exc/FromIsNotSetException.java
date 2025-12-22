package io.datlin.sql.exc;

public class FromIsNotSetException extends RuntimeException {

    public FromIsNotSetException() {
        super("From is not set");
    }
}