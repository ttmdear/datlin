package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents a single assignment within a SQL statement, typically used in the SET clause of an UPDATE
 * or the column/value mapping of an INSERT.
 * <p>
 * This record maps a specific {@link ColumnReference} to its target value.
 *
 * @param column the reference to the database column being assigned a value
 * @param value  the value to be assigned to the column, which may be null
 */
public record Assignment(
    @Nonnull ColumnReference column,
    @Nullable Object value
) implements SqlFragment {

    /**
     * Static factory method to create a new Assignment.
     *
     * @param column the target column reference
     * @param value  the value to assign
     * @return a new Assignment instance
     */
    @Nonnull
    public static Assignment set(
        @Nonnull final ColumnReference column,
        @Nullable final Object value
    ) {
        return new Assignment(column, value);
    }
}
