package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

import static java.util.List.copyOf;

/**
 * Represents a SQL UPDATE statement.
 * <p>
 * This record defines the target table, the list of column assignments (SET clause),
 * and the criteria used to filter which rows should be updated (WHERE clause).
 *
 * @param table the table to be updated
 * @param sets  the list of column-to-value assignments
 * @param where the criteria defining which rows to update
 * @since 1.0.0
 */
public record Update(
    @Nullable TableReference table,
    @Nonnull List<Assignment> sets,
    @Nullable Criteria where
) implements SqlFragment {

    /**
     * Create instance of {@link Update} query.
     *
     * @return the update
     */
    @Nonnull
    public static Update update() {
        return new Update(null, List.of(), null);
    }

    /**
     * Creates a new Update instance with the specified target table.
     *
     * @param table the target table reference
     * @return a new Update instance
     */
    @Nonnull
    public Update table(@Nonnull final TableReference table) {
        return new Update(table, sets, where);
    }

    /**
     * Creates a new Update instance with the provided list of assignments.
     *
     * @param sets the list of assignments for the SET clause
     * @return a new Update instance
     */
    @Nonnull
    public Update sets(@Nonnull final List<Assignment> sets) {
        return new Update(table, copyOf(sets), where);
    }

    /**
     * Creates a new Update instance with the specified filtering criteria.
     *
     * @param where the criteria for the WHERE clause
     * @return a new Update instance
     */
    @Nonnull
    public Update where(@Nonnull final SqlFragment where) {
        return new Update(
            table,
            sets,
            where instanceof Criteria ? (Criteria) where : Criteria.and(where)
        );
    }

    /**
     * Creates a new Update instance with the specified filtering criteria.
     *
     * @param where the criteria for the WHERE clause
     * @return a new Update instance
     */
    @Nonnull
    public Update where(@Nonnull final SqlFragment... where) {
        return new Update(
            table,
            sets,
            Criteria.and(where)
        );
    }

    /**
     * Creates a new Update instance with the specified filtering criteria.
     *
     * @param where the criteria for the WHERE clause
     * @return a new Update instance
     */
    @Nonnull
    public Update where(@Nonnull final List<SqlFragment> where) {
        return new Update(
            table,
            sets,
            Criteria.and(where)
        );
    }
}
