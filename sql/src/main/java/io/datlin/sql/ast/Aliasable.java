package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Generic contract for SQL elements that support aliasing.
 * <p>
 * This interface provides a fluent way to manage SQL aliases for various database
 * components (e.g., columns, tables, or subqueries). It is designed to work
 * seamlessly with immutable objects like Java Records.
 * </p>
 *
 * @param <T> The type of the object implementing this interface, ensuring
 *            type safety for method chaining.
 */
public interface Aliasable<T> {

    /**
     * Retrieves the current alias assigned to this element.
     * <p>
     * In the context of a SQL statement, this represents the identifier
     * following the {@code AS} keyword.
     * </p>
     *
     * @return The non-null string representing the alias. If no alias exists,
     * it should return an empty string or a default identifier.
     */
    @Nullable
    String alias();

    /**
     * Creates a new instance of the element with the specified alias.
     * <p>
     * This is a "wither" method. Since implementing classes (like Records)
     * are typically immutable, this method returns a copy of the original
     * object with the new alias applied, preserving the original state.
     * </p>
     *
     * @param alias The new alias name to assign. Must not be null.
     * @return A new instance of type {@code T} with the updated alias.
     */
    @Nonnull
    T as(@Nonnull final String alias);
}