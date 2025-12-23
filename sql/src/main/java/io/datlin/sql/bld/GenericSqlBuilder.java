package io.datlin.sql.bld;

import io.datlin.sql.ast.Aliasable;
import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.Comparison;
import io.datlin.sql.ast.ComparisonOperator;
import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.Insert;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.ast.Update;
import io.datlin.sql.ast.UpdateSetNode;
import io.datlin.sql.ast.Value;
import io.datlin.sql.exc.FromIsNotSetException;
import io.datlin.sql.exc.InsertColumnsNotSetException;
import io.datlin.sql.exc.InsertValuesNumberIsDifferentThenColumnsException;
import io.datlin.sql.exc.UpdateSetIsNotSetException;
import jakarta.annotation.Nonnull;

import java.util.List;

public class GenericSqlBuilder implements SqlBuilder {

    /**
     * Identifier Quote Character (IQC).
     * <p>
     * This character is used to delimit (quote) SQL identifiers such as table names,
     * column names, and schema names. Quoting identifiers is necessary when:
     * </p>
     * <ul>
     * <li>The identifier is a reserved SQL keyword (e.g., {@code "ORDER"}, {@code "USER"}).</li>
     * <li>The identifier contains spaces or special characters.</li>
     * <li>Case sensitivity needs to be preserved in certain databases (e.g., PostgreSQL).</li>
     * </ul>
     * * <p><b>Common implementations:</b></p>
     * <ul>
     * <li>{@code '"'} (Double Quote) - ANSI SQL standard, used by <b>PostgreSQL, Oracle, SQLite, H2</b>.</li>
     * <li>{@code '`'} (Backtick) - Used by <b>MySQL, MariaDB</b>.</li>
     * </ul>
     */
    private final char iqc = '"';

    public void build(
        @Nonnull final Object fragment,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (fragment instanceof Select) {
            build((Select) fragment, sql, context);
        } else if (fragment instanceof Criteria) {
            build((Criteria) fragment, sql, context);
        } else if (fragment instanceof Comparison) {
            build((Comparison) fragment, sql, context);
        } else if (fragment instanceof Value) {
            build((Value) fragment, sql, context);
        } else if (fragment instanceof ColumnReference) {
            build((ColumnReference) fragment, sql, context);
        } else {

        }
    }

    @Override
    public void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("SELECT");

        // columns -----------------------------------------------------------------------------------------------------
        if (select.columns().isEmpty()) {
            sql.append(" *");
        } else {
            for (int i = 0; i < select.columns().size(); i++) {
                final SqlFragment column = select.columns().get(i);

                sql.append(" ");

                if (column instanceof ColumnReference columnReference) {
                    build(columnReference, sql, context);
                } else {
                    sql.append(" (");
                    build(column, sql, context);
                    sql.append(")");
                }

                if (select.columns().get(i) instanceof Aliasable<?> aliasable) {
                    appendAlias(sql, aliasable);
                }

                if (i < select.columns().size() - 1) {
                    sql.append(",");
                }
            }
        }

        // from --------------------------------------------------------------------------------------------------------
        final SqlFragment from = select.from();
        if (from == null) {
            throw new FromIsNotSetException();
        }

        sql.append(" FROM");

        if (from instanceof TableReference tableReference) {
            sql.append(" ");
            build(tableReference, sql, context);
        } else {
            sql.append(" (");
            build(from, sql, context);
            sql.append(")");
        }

        if (from instanceof Aliasable<?> aliasable) {
            appendAlias(sql, aliasable);
        }

        // where -------------------------------------------------------------------------------------------------------
        if (select.where() != null) {
            final StringBuilder whereSql = new StringBuilder();
            build(select.where(), whereSql, context);

            if (!whereSql.isEmpty()) {
                sql.append(" WHERE ").append(whereSql);
            }
        }
    }

    @Override
    public void build(
        @Nonnull final Insert insert,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("INSERT INTO ");
        build(insert.into(), sql, context);

        if (insert.columns().isEmpty()) {
            throw new InsertColumnsNotSetException(insert.into().name());
        }

        sql.append(" (");
        for (int i = 0; i < insert.columns().size(); i++) {
            sql.append(insert.columns().get(i));

            if (i < insert.columns().size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        if (insert.values().isEmpty()) {
            throw new InsertColumnsNotSetException(insert.into().name());
        }

        sql.append(" VALUES");

        for (int i = 0; i < insert.values().size(); i++) {
            final List<Object> values = insert.values().get(i);

            if (values.size() != insert.columns().size()) {
                throw new InsertValuesNumberIsDifferentThenColumnsException();
            }

            sql.append(" (");

            for (int i1 = 0; i1 < values.size(); i1++) {
                final Object value = values.get(i1);

                if (value instanceof SqlFragment sqlFragment) {
                    build((sqlFragment), sql, context);
                } else {
                    context.addStatementObjects(value);
                    sql.append("?");
                }

                if (i1 < values.size() - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")");

            if (i < insert.values().size() - 1) {
                sql.append(", ");
            }
        }
    }

    @Override
    public void build(
        @Nonnull final Update update,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        // update ------------------------------------------------------------------------------------------------------
        sql.append("UPDATE ");
        build(update.table(), sql, context);

        if (update.sets().isEmpty()) {
            throw new UpdateSetIsNotSetException(update.table().name());
        }

        // set ---------------------------------------------------------------------------------------------------------
        sql.append(" SET ");
        for (int i = 0; i < update.sets().size(); i++) {
            final UpdateSetNode set = update.sets().get(i);
            sql.append("\"").append(set.column()).append("\"").append(" = ");

            if (set.value() == null) {
                sql.append("NULL");
            } else if (set.value() instanceof SqlFragment sqlFragment) {
                sql.append("(");
                build((sqlFragment), sql, context);
                sql.append(")");
            } else {
                context.addStatementObjects(set.value());
                sql.append("?");
            }

            if (i < update.sets().size() - 1) {
                sql.append(", ");
            }
        }

        // where -------------------------------------------------------------------------------------------------------
        if (update.where() != null) {
            final StringBuilder whereSql = new StringBuilder();
            build(update.where(), whereSql, context);

            if (!whereSql.isEmpty()) {
                sql.append(" WHERE ").append(whereSql);
            }
        }
    }

    @Override
    public void build(
        @Nonnull final Delete delete,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        // delete ------------------------------------------------------------------------------------------------------
        sql.append("DELETE FROM ");
        build(delete.table(), sql, context);

        // where -------------------------------------------------------------------------------------------------------
        if (delete.where() != null) {
            final StringBuilder whereSql = new StringBuilder();
            build(delete.where(), whereSql, context);

            if (!whereSql.isEmpty()) {
                sql.append(" WHERE ").append(whereSql);
            }
        }
    }

    public void build(
        @Nonnull final Criteria criteria,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        for (int i = 0; i < criteria.criteria().size(); i++) {
            final SqlFragment criterion = criteria.criteria().get(i);
            sql.append("(");
            build(criterion, sql, context);
            sql.append(")");

            if (i < criteria.criteria().size() - 1) {
                sql.append(" ").append(criteria.operator());
            }
        }
    }

    public void build(
        @Nonnull final Comparison comparison,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        build(comparison.left(), sql, context);
        sql.append(" ").append(resolveComparisonOperator(comparison.operator())).append(" ");
        build(comparison.right(), sql, context);
    }

    public void build(
        @Nonnull final Value value,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        context.addStatementObjects(value.value());
        sql.append("?");
    }

    public void build(
        @Nonnull final ColumnReference columnReference,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (columnReference.from() != null) {
            appendInIqc(sql, columnReference.from());
            sql.append(".");
        }

        appendInIqc(sql, columnReference.column());
    }

    public void build(
        @Nonnull final TableReference tableLiteral,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (tableLiteral.schema() != null) {
            sql.append("\"").append(tableLiteral.schema()).append("\"");
            sql.append(".");
        }

        sql.append("\"").append(tableLiteral.name()).append("\"");
    }

    @Nonnull
    private String resolveComparisonOperator(@Nonnull final ComparisonOperator operator) {
        return switch (operator) {
            case EQ -> "=";
            case NEQ -> "<>"; // Lub "!="
            case GT -> ">";
            case GTE -> ">=";
            case LT -> "<";
            case LTE -> "<=";
        };
    }

    /**
     * Appends a string value to the {@link StringBuilder} wrapped in the Identifier Quote Character (IQC).
     * <p>
     * This helper method is used to safely add database identifiers (such as table or column names)
     * to a dynamic SQL query. By wrapping the value in {@link #iqc}, it ensures that the identifier
     * is correctly interpreted by the database engine, even if it is a reserved keyword or
     * contains special characters.
     * </p>
     * * <p><b>Example:</b></p>
     * If {@code iqc} is {@code '"'} and {@code value} is {@code "user_table"},
     * it appends {@code "user_table"} (including quotes) to the SQL buffer.
     *
     * @param sql   The {@link StringBuilder} buffer where the quoted identifier will be appended.
     *              Must not be null.
     * @param value The raw identifier name (table, column, etc.) to be quoted.
     *              Must not be null.
     */
    private void appendInIqc(
        @Nonnull final StringBuilder sql,
        @Nonnull final String value
    ) {
        sql.append(iqc).append(value).append(iqc);
    }

    /**
     * Appends the SQL {@code AS} clause to the query buffer if an alias is present.
     * <p>
     * This method checks the provided {@link Aliasable} element for a defined alias.
     * If the alias is present (not null and not empty), it appends the {@code AS}
     * keyword followed by the quoted alias name using {@link #appendInIqc}.
     * </p>
     *
     * <p><b>SQL Output Example:</b></p>
     * <ul>
     * <li>With alias "total": {@code " AS \"total\""}</li>
     * <li>With empty alias: {@code ""} (nothing is appended)</li>
     * </ul>
     *
     * @param sql       The {@link StringBuilder} buffer representing the SQL query being built.
     *                  Must not be null.
     * @param aliasable The element that may contain an alias (e.g., Column, Value, or Subquery).
     *                  Must not be null.
     * @see #appendInIqc(StringBuilder, String)
     */
    private void appendAlias(
        @Nonnull final StringBuilder sql,
        @Nonnull final Aliasable<?> aliasable
    ) {
        final String as = aliasable.alias();

        if (as == null || as.isEmpty()) {
            return;
        }

        sql.append(" AS ");
        appendInIqc(sql, as);
    }
}