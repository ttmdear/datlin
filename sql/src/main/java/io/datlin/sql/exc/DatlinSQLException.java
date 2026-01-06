package io.datlin.sql.exc;

import jakarta.annotation.Nonnull;

/**
 * Custom runtime exception thrown when an error occurs during the execution or
 * fetching of a SQL query within the Datlin framework.
 * <p>
 * This exception wraps the original {@link Exception} and provides additional
 * context by including the specific SQL statement that triggered the failure.
 * </p>
 */
public class DatlinSQLException extends RuntimeException {

    /**
     * Constructs a new DatlinSQLException with a detailed message containing the failed SQL query.
     *
     * @param sql   the SQL query string that was being executed when the error occurred
     * @param cause the underlying exception that caused the failure
     */
    public DatlinSQLException(
        @Nonnull final String sql,
        @Nonnull final Exception cause
    ) {
        super("Error during fetching: " + sql, cause);
    }

    /**
     * Constructs a new DatlinSQLException with a custom message and an underlying cause.
     *
     * @param message the detail message
     * @param cause   the underlying exception that caused the failure
     */
    public DatlinSQLException(
        @Nonnull final String message,
        @Nonnull final Throwable cause
    ) {
        super(message, cause);
    }

    /**
     * Constructs a new DatlinSQLException with a custom message and an underlying cause.
     *
     * @param message the detail message
     */
    public DatlinSQLException(
        @Nonnull final String message
    ) {
        super(message);
    }

    /**
     * Constructs a new DatlinSQLException with only an underlying cause.
     *
     * @param cause the underlying exception that caused the failure
     */
    public DatlinSQLException(@Nonnull final Throwable cause) {
        super(cause);
    }
}
