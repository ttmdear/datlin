package io.datlin.sql.logger;

import io.datlin.sql.exc.DatlinSqlExecuteException;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;

import java.sql.SQLException;

/**
 * Utility class for standardized logging of SQL execution errors.
 * <p>
 * This class extracts detailed information from {@link DatlinSqlExecuteException},
 * including specific SQL states and error codes if the underlying cause
 * is a {@link java.sql.SQLException}.
 * </p>
 */
public class DatlinLogger {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DatlinLogger() {
    }

    /**
     * Logs detailed error information regarding a failed SQL query execution.
     * <p>
     * If the provided exception's cause is an instance of {@link java.sql.SQLException},
     * the log entry will include the SQL State and Vendor Error Code. Otherwise,
     * a simplified error message with the SQL query is logged.
     * </p>
     *
     * @param exception the exception containing the execution failure details.
     * @param log       the SLF4J logger instance to be used for recording the error.
     * @param sql       the raw SQL query string that triggered the failure.
     * @throws DatlinSqlExecuteException rethrows the provided exception to allow
     *                                   caller-side error handling after logging.
     */
    public static DatlinSqlExecuteException logAndReturn(
        @Nonnull final DatlinSqlExecuteException exception,
        @Nonnull final Logger log,
        @Nonnull final String sql
    ) throws DatlinSqlExecuteException {
        if (exception.getCause() instanceof SQLException sqlException) {
            log.error("SQL Execution failed!\n" +
                    "Query: {}\n" +
                    "SQL State: {}\n" +
                    "Error Code: {}\n" +
                    "Message: {}",
                sql, sqlException.getSQLState(), sqlException.getErrorCode(), exception.getMessage(), exception);
        } else {
            log.error("SQL Execution failed!\n" +
                    "Query: {}\n" +
                    "Message: {}",
                sql, exception.getMessage(), exception);
        }

        throw exception;
    }
}
