package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a group of SQL conditions joined by a logical operator (AND/OR).
 * This record implements {@link SqlFragment}, allowing it to be used recursively
 * to build complex nested query structures.
 *
 * @param operator The logical operator used to join the criteria.
 * @param criteria The list of SQL fragments to be joined.
 */
public record Criteria(
    @Nonnull LogicalOperator operator,
    @Nonnull List<SqlFragment> criteria
) implements SqlFragment {

    // and -------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new Criteria object that joins the provided fragments with an {@code AND} operator.
     *
     * @param criteria Variable number of {@link SqlFragment} objects to be combined.
     * @return A new {@link Criteria} instance representing the logical conjunction.
     */
    @Nonnull
    public static Criteria and(
        @Nonnull final SqlFragment... criteria
    ) {
        return new Criteria(LogicalOperator.AND, Arrays.stream(criteria).toList());
    }

    /**
     * Creates a new Criteria object that joins the provided list of fragments with an {@code AND} operator.
     *
     * @param criteria A list of {@link SqlFragment} objects to be combined.
     * @return A new {@link Criteria} instance representing the logical conjunction.
     */
    @Nonnull
    public static Criteria and(
        @Nonnull final List<SqlFragment> criteria
    ) {
        return new Criteria(LogicalOperator.AND, List.copyOf(criteria));
    }

    // or --------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new Criteria object that joins the provided fragments with an {@code OR} operator.
     *
     * @param criteria Variable number of {@link SqlFragment} objects to be combined.
     * @return A new {@link Criteria} instance representing the logical disjunction.
     */
    @Nonnull
    public static Criteria or(
        @Nonnull final SqlFragment... criteria
    ) {
        return new Criteria(LogicalOperator.OR, Arrays.stream(criteria).toList());
    }

    /**
     * Creates a new Criteria object that joins the provided list of fragments with an {@code OR} operator.
     *
     * @param criteria A list of {@link SqlFragment} objects to be combined.
     * @return A new {@link Criteria} instance representing the logical disjunction.
     */
    @Nonnull
    public static Criteria or(
        @Nonnull final List<SqlFragment> criteria
    ) {
        return new Criteria(LogicalOperator.OR, List.copyOf(criteria));
    }
}
