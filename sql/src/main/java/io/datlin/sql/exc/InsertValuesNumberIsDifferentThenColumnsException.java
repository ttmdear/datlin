package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

public class InsertValuesNumberIsDifferentThenColumnsException extends RuntimeException {
    public InsertValuesNumberIsDifferentThenColumnsException() {
        super("Insert values number is different to columns number.");
    }
}
