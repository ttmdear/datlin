package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

import static java.util.List.copyOf;

/**
 * Represents a SQL INSERT statement fragment.
 * <p>
 * This record is immutable. Use the fluent "wither" methods to create new instances
 * with modified properties.
 *
 * @param columns the list of target column names
 * @param values  a list of rows, where each row is a list of values to be inserted
 * @param into    the target table reference
 */
public record Insert(
    @Nonnull List<String> columns,
    @Nonnull List<List<Object>> values,
    @Nullable TableReference into
) implements SqlFragment {

    /**
     * Creates an empty {@code Insert} instance.
     *
     * @return a new, empty Insert fragment
     */
    @Nonnull
    public static Insert insert() {
        return new Insert(List.of(), List.of(), null);
    }

    /**
     * Sets the target table for the INSERT statement.
     *
     * @param table the table reference
     * @return a new Insert instance with the specified table
     */
    @Nonnull
    public Insert into(@Nonnull final TableReference table) {
        return new Insert(columns, values, table);
    }

    /**
     * Sets the columns for the INSERT statement.
     *
     * @param columns the list of column names
     * @return a new Insert instance with the specified columns
     */
    @Nonnull
    public Insert columns(@Nonnull final List<String> columns) {
        return new Insert(copyOf(columns), values, into);
    }

    /**
     * Sets the values to be inserted.
     *
     * @param values a list of rows to insert
     * @return a new Insert instance with the specified values
     */
    @Nonnull
    public Insert values(@Nonnull final List<List<Object>> values) {
        return new Insert(this.columns, copyOf(values), this.into);
    }
}
