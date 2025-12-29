package io.datlin.sql;

import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.GenericSqlBuilder;
import jakarta.annotation.Nonnull;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Utility class providing custom assertions for SQL generation testing.
 * <p>
 * This class facilitates the validation of SQL fragments produced by the library,
 * ensuring that the structural translation of criteria or expressions matches
 * the expected SQL output.
 * </p>
 *
 * @since 1.0.0
 */
public class SqlAssertions {

    private SqlAssertions() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Asserts that the provided SQL fragment builds into the expected SQL string.
     * <p>
     * This method initializes a fresh {@link BuildContext} and {@link StringBuilder},
     * invokes the build process for the given {@code sqlFragment}, and compares
     * the result against the {@code expected} string using a standard equality assertion.
     * </p>
     *
     * @param expected    the expected SQL string to compare against; must not be {@code null}
     * @param sqlBuilder  the builder responsible for orchestrating the build process;
     *                    must not be {@code null}
     * @param sqlFragment the source object (e.g., Criteria, Token, or Expression)
     *                    to be translated into SQL; must not be {@code null}
     * @throws AssertionError if the generated SQL does not match the expected string
     */
    public static void assertSql(
        @Nonnull final String expected,
        @Nonnull final GenericSqlBuilder sqlBuilder,
        @Nonnull final Object sqlFragment
    ) {
        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();

        sqlBuilder.build(sqlFragment, sql, buildContext);

        assertEquals(expected, sql.toString());
    }

    public static void assertSql(
        @Nonnull final String expectedSql,
        @Nonnull final List<?> expectedBinds,
        @Nonnull final GenericSqlBuilder sqlBuilder,
        @Nonnull final Object sqlFragment
    ) {
        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();
        sqlBuilder.build(sqlFragment, sql, buildContext);
        assertEquals(expectedSql, sql.toString());

        assertEquals(expectedBinds.size(), buildContext.getStatementObjects().size());

        for (int i = 0; i < expectedBinds.size(); i++) {
            assertEquals(expectedBinds.get(i), buildContext.getStatementObjects().get(i));
        }
    }
}
