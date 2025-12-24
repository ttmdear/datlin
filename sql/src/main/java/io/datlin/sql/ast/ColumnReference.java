package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents a reference to a database column, optionally qualified by a table name
 * or associated with an alias.
 * <p>
 * This record is used to generate column identifiers in SQL statements, such as
 * {@code table_name.column_name} or {@code column_name AS alias}.
 *
 * @param from   the table name or table alias from which this column originates (optional)
 * @param column the name of the column
 * @param alias  the alias assigned to the column for the result set (optional)
 */
public record ColumnReference(
    @Nullable String from,
    @Nonnull String column,
    @Nullable String alias
) implements SqlFragment, Aliasable<ColumnReference>, Comparable<ColumnReference> {

    /**
     * Creates a new {@code ColumnReference} by qualifying the current column with a table name.
     *
     * @param table the table name or table alias to qualify the column
     * @return a new ColumnReference instance with the specified qualifier
     */
    @Nonnull
    public ColumnReference from(@Nonnull final String table) {
        return new ColumnReference(table, column, alias);
    }

    /**
     * Static factory method to create a basic {@code ColumnReference} using only the column name.
     *
     * @param column the name of the database column
     * @return a new ColumnReference instance
     */
    @Nonnull
    public static ColumnReference column(@Nonnull final String column) {
        return new ColumnReference(null, column, null);
    }

    /**
     * Creates a new {@code ColumnReference} with the specified alias.
     * <p>
     * Implements the {@link Aliasable#as(String)} method.
     *
     * @param as the alias name
     * @return a new ColumnReference instance with the specified alias
     */
    @Override
    @Nonnull
    public ColumnReference as(@Nonnull final String as) {
        return new ColumnReference(from, column, as);
    }
}
