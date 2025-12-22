package io.datlin.sql.exc;

public class UpdateTableIsNotSetException extends RuntimeException {
    public UpdateTableIsNotSetException() {
        super("Update from is not set");
    }
}
