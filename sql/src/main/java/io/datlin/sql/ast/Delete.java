package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Represents a SQL DELETE statement.
 * <p>
 * This record defines the target table from which rows will be removed and
 * the criteria that determines which specific rows are affected.
 *
 * @param table the table from which to delete rows
 * @param where the criteria defining the scope of the deletion (WHERE clause)
 */
public record Delete(
    @Nullable TableReference table,
    @Nullable Criteria where
) implements SqlFragment {

    /**
     * Creates an empty {@code Delete} instance.
     *
     * @return a new, empty Delete fragment
     */
    @Nonnull
    public static Delete delete() {
        return new Delete(null, null);
    }

    /**
     * Creates a new Delete instance with the specified target table.
     *
     * @param table the table reference to delete from
     * @return a new Delete instance with the updated table
     */
    @Nonnull
    public Delete from(@Nonnull final TableReference table) {
        return new Delete(table, where);
    }

    /**
     * Sets the filtering criteria for the DELETE statement.
     * <p>
     * If the provided fragment is already an instance of {@link Criteria}, it is used directly.
     * Otherwise, the fragment is wrapped in a logical AND container.
     *
     * @param where the SQL fragment representing the WHERE condition
     * @return a new Delete instance with the specified criteria
     */
    @Nonnull
    public Delete where(@Nonnull final SqlFragment where) {
        return new Delete(
            table,
            where instanceof Criteria ? (Criteria) where : Criteria.and(where)
        );
    }

    /**
     * Sets the filtering criteria by combining multiple SQL fragments using logical AND.
     *
     * @param where one or more SQL fragments to be joined by AND logic
     * @return a new Delete instance with the combined criteria
     */
    @Nonnull
    public Delete where(@Nonnull final SqlFragment... where) {
        return new Delete(
            table,
            Criteria.and(where)
        );
    }

    /**
     * Sets the filtering criteria by combining a list of SQL fragments using logical AND.
     *
     * @param where a list of SQL fragments to be joined by AND logic
     * @return a new Delete instance with the combined criteria
     */
    @Nonnull
    public Delete where(@Nonnull final List<SqlFragment> where) {
        return new Delete(
            table,
            Criteria.and(where)
        );
    }
}
