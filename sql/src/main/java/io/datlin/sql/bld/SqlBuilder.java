package io.datlin.sql.bld;

import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.Insert;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.Update;
import jakarta.annotation.Nonnull;

/**
 * Strategy interface for building SQL statements from domain-specific model objects.
 * <p>
 * This builder follows the Visitor pattern, providing specific build methods for
 * different types of SQL operations (DML). It encapsulates the logic required
 * to transform structured query objects into their raw string representations
 * while maintaining state via a {@link BuildContext}.
 */
public interface SqlBuilder {

    /**
     * Translates an {@link Insert} model into a SQL string.
     *
     * @param insert  the structured representation of the INSERT statement.
     * @param sql     the string builder where the generated SQL will be appended.
     * @param context the current build context, providing metadata or state (e.g., parameter mapping).
     */
    void build(
        @Nonnull final Insert insert,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    /**
     * Translates a {@link Select} model into a SQL string.
     *
     * @param select  the structured representation of the SELECT statement.
     * @param sql     the string builder where the generated SQL will be appended.
     * @param context the current build context, providing metadata or state.
     */
    void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    /**
     * Translates an {@link Update} model into a SQL string.
     *
     * @param update  the structured representation of the UPDATE statement.
     * @param sql     the string builder where the generated SQL will be appended.
     * @param context the current build context, providing metadata or state.
     */
    void build(
        @Nonnull final Update update,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    /**
     * Translates a {@link Delete} model into a SQL string.
     *
     * @param delete  the structured representation of the DELETE statement.
     * @param sql     the string builder where the generated SQL will be appended.
     * @param context the current build context, providing metadata or state.
     */
    void build(
        @Nonnull final Delete delete,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );
}
