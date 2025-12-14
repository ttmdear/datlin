package io.datlin.sql.exc;

public class DeleteTableIsNotSetException extends RuntimeException {
    public DeleteTableIsNotSetException() {
        super("Delete table is not set");
    }
}
