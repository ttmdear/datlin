package io.datlin.sql.exc;

public class InsertIntoNotSetException extends RuntimeException {
    public InsertIntoNotSetException() {
        super("Insert into is not set.");
    }
}
