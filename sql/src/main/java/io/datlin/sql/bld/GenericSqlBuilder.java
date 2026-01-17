package io.datlin.sql.bld;

import io.datlin.sql.ast.Aliasable;
import io.datlin.sql.ast.Assignment;
import io.datlin.sql.ast.BinaryExpression;
import io.datlin.sql.ast.BinaryOperator;
import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.FunctionCall;
import io.datlin.sql.ast.InExpression;
import io.datlin.sql.ast.Insert;
import io.datlin.sql.ast.PostgresUpsert;
import io.datlin.sql.ast.RawValue;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.ast.UnaryExpression;
import io.datlin.sql.ast.UnaryOperator;
import io.datlin.sql.ast.Update;
import io.datlin.sql.ast.ValueReference;
import io.datlin.sql.exc.DatlinSqlPrepareException;
import jakarta.annotation.Nonnull;

import java.util.List;

import static io.datlin.sql.ast.ValueReference.value;

/**
 * A versatile implementation of the {@link SqlBuilder} interface designed to handle
 * various SQL fragments.
 * <p>
 * This builder acts as a central dispatcher that translates high-level objects
 * (such as criteria, tokens, or expressions) into their corresponding SQL representations.
 * It manages the recursive traversal of complex expression trees and ensures that
 * the build state is correctly maintained within the {@link BuildContext}.
 * </p>
 *
 * @see SqlBuilder
 * @see BuildContext
 * @since 1.0.0
 */
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
        if (fragment instanceof Select select) {
            build(select, sql, context);
        } else if (fragment instanceof Update update) {
            build(update, sql, context);
        } else if (fragment instanceof Criteria criteria) {
            build(criteria, sql, context);
        } else if (fragment instanceof BinaryExpression binaryExpression) {
            build(binaryExpression, sql, context);
        } else if (fragment instanceof InExpression inExpression) {
            build(inExpression, sql, context);
        } else if (fragment instanceof UnaryExpression unaryExpression) {
            build(unaryExpression, sql, context);
        } else if (fragment instanceof ValueReference valueReference) {
            build(valueReference, sql, context);
        } else if (fragment instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else if (fragment instanceof RawValue rawValue) {
            build(rawValue, sql, context);
        } else if (fragment instanceof FunctionCall functionCall) {
            build(functionCall, sql, context);
        } else if (fragment instanceof PostgresUpsert postgresUpsert) {
            build(postgresUpsert, sql, context);
        } else {
            throw new DatlinSqlPrepareException("Unknown type of sql fragment '%s'".formatted(fragment.getClass().getSimpleName()));
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
            throw new DatlinSqlPrepareException("FROM for SELECT statement is not set", sql.toString());
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
            throw new DatlinSqlPrepareException(
                "No columns set for INSERT INTO '%s'".formatted(insert.into().name()),
                sql.toString()
            );
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
            throw new DatlinSqlPrepareException(
                "No values set for INSERT INTO '%s'".formatted(insert.into().name()),
                sql.toString()
            );
        }

        sql.append(" VALUES");

        for (int i = 0; i < insert.values().size(); i++) {
            final List<Object> values = insert.values().get(i);

            if (values.size() != insert.columns().size()) {
                throw new DatlinSqlPrepareException(
                    "Incorrect number of columns and values for INSERT INTO '%s'".formatted(insert.into().name()),
                    sql.toString()
                );
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
        @Nonnull final PostgresUpsert insert,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("INSERT INTO ");

        final TableReference into = insert.into();

        if (into == null) {
            throw new DatlinSqlPrepareException("INTO INSERT is not set", sql.toString());
        }

        appendInIqc(sql, into.name());

        if (insert.columns().isEmpty()) {
            throw new DatlinSqlPrepareException(
                "No columns set for INSERT INTO '%s'".formatted(into.name()),
                sql.toString()
            );
        }

        sql.append(" (");
        for (int i = 0; i < insert.columns().size(); i++) {
            appendInIqc(
                sql,
                insert.columns().get(i).column()
            );

            if (i < insert.columns().size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        if (insert.values().isEmpty()) {
            throw new DatlinSqlPrepareException(
                "No values set for INSERT INTO '%s'".formatted(into.name()),
                sql.toString()
            );
        }

        sql.append(" VALUES");

        for (int i = 0; i < insert.values().size(); i++) {
            final List<Object> values = insert.values().get(i);

            if (values.size() != insert.columns().size()) {
                throw new DatlinSqlPrepareException(
                    "Incorrect number of columns and values for INSERT INTO '%s'".formatted(into.name()),
                    sql.toString()
                );
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

        final List<ColumnReference> onConflictColumns = insert.onConflictColumns();
        final String onConflictConstraint = insert.onConflictConstraint();
        final List<Assignment> doUpdate = insert.doUpdate();
        final Criteria onConflictWhere = insert.onConflictWhere();
        final Boolean doNothing = insert.doNothing();
        final Boolean onAnyConflict = onConflictColumns != null || onConflictConstraint != null;

        if (onConflictColumns != null) {
            sql.append(" ON CONFLICT (");

            for (int i = 0; i < onConflictColumns.size(); i++) {
                appendInIqc(sql, onConflictColumns.get(i).column());

                if (i < onConflictColumns.size() - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")");
        } else if (onConflictConstraint != null) {
            sql.append(" ON CONFLICT ON CONSTRAINT").append(onConflictConstraint);
        }

        if (onAnyConflict && doUpdate != null) {
            sql.append(" DO UPDATE SET ");

            for (int i = 0; i < doUpdate.size(); i++) {
                final Assignment set = doUpdate.get(i);

                sql.append("\"").append(set.column().column()).append("\"").append(" = ");

                if (set.value() == null) {
                    sql.append("NULL");
                } else if (set.value() instanceof RawValue rawValue) {
                    sql.append(rawValue.value());
                } else if (set.value() instanceof SqlFragment sqlFragment) {
                    sql.append("(");
                    build((sqlFragment), sql, context);
                    sql.append(")");
                } else {
                    context.addStatementObjects(set.value());
                    sql.append("?");
                }

                if (i < doUpdate.size() - 1) {
                    sql.append(", ");
                }
            }
        }

        // WHERE -------------------------------------------------------------------------------------------------------
        if (onAnyConflict && onConflictWhere != null) {
            final StringBuilder whereSql = new StringBuilder();
            build(onConflictWhere, whereSql, context);

            if (!whereSql.isEmpty()) {
                sql.append(" WHERE ").append(whereSql);
            }
        } else if (onAnyConflict && doNothing != null && doNothing) {
            sql.append(" DO NOTHING");
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

        final TableReference table = update.table();

        if (table == null) {
            throw new DatlinSqlPrepareException("No table set for UPDATE", sql.toString());
        }

        sql.append("\"").append(table.name()).append("\"");

        if (update.sets().isEmpty()) {
            throw new DatlinSqlPrepareException(
                "No SET's for UPDATE table '%s'".formatted(table.name()),
                sql.toString()
            );
        }

        // set ---------------------------------------------------------------------------------------------------------
        sql.append(" SET ");
        for (int i = 0; i < update.sets().size(); i++) {
            final Assignment set = update.sets().get(i);

            sql.append("\"").append(set.column().column()).append("\"").append(" = ");

            if (set.value() == null) {
                sql.append("NULL");
            } else if (set.value() instanceof RawValue rawValue) {
                sql.append(rawValue.value());
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
        @Nonnull final FunctionCall functionCall,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append(functionCall.function()).append("(");

        for (int i = 0; i < functionCall.arguments().size(); i++) {
            final SqlFragment argument = functionCall.arguments().get(i);

            if (argument instanceof RawValue rawValue) {
                build(rawValue, sql, context);
            } else if (argument instanceof ValueReference valueReference) {
                build(valueReference, sql, context);
            } else if (argument instanceof TableReference tableReference) {
                build(tableReference, sql, context);
            } else if (argument instanceof ColumnReference columnReference) {
                build(columnReference, sql, context);
            } else {
                sql.append("(");
                build(argument, sql, context);
                sql.append(")");
            }

            if (i < functionCall.arguments().size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
    }

    /**
     * Translates the given criteria into a SQL fragment and appends it to the query buffer.
     * <p>
     * This method processes the logical {@code Criteria} definition, generates the
     * corresponding SQL syntax, and manages query parameters or aliases through the
     * provided {@code BuildContext}.
     * </p>
     *
     * @param criteria the criteria definition (e.g., comparison, logical expression)
     *                 to be processed; must not be {@code null}
     * @param sql      the buffer to which the generated SQL fragment will be appended;
     *                 must not be {@code null}
     * @param context  the shared build state, used for tracking bind parameters,
     *                 table aliases, and dialect-specific settings; must not be {@code null}
     */
    public void build(
        @Nonnull final Criteria criteria,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        for (int i = 0; i < criteria.criteria().size(); i++) {
            final SqlFragment criterion = criteria.criteria().get(i);

            if (criterion instanceof ColumnReference columnReference) {
                build(columnReference, sql, context);
            } else if (criterion instanceof BinaryExpression binaryExpression) {
                build(binaryExpression, sql, context);
            } else {
                sql.append("(");
                build(criterion, sql, context);
                sql.append(")");
            }

            if (i < criteria.criteria().size() - 1) {
                sql.append(" ").append(criteria.operator()).append(" ");
            }
        }
    }

    public void build(
        @Nonnull final BinaryExpression binaryExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (binaryExpression.left() instanceof ValueReference valueReference) {
            build(valueReference, sql, context);
        } else if (binaryExpression.left() instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else if (binaryExpression.left() instanceof SqlFragment sqlFragment) {
            appendInBrackets(sql, sqlFragment, context);
        } else {
            build(value(binaryExpression.left()), sql, context);
        }

        sql.append(" ").append(resolveComparisonOperator(binaryExpression.operator())).append(" ");

        if (binaryExpression.right() instanceof ValueReference valueReference) {
            build(valueReference, sql, context);
        } else if (binaryExpression.right() instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else if (binaryExpression.right() instanceof SqlFragment sqlFragment) {
            appendInBrackets(sql, sqlFragment, context);
        } else {
            build(value(binaryExpression.right()), sql, context);
        }
    }

    public void build(
        @Nonnull final UnaryExpression expression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (expression.operator().equals(UnaryOperator.NOT) ||
            expression.operator().equals(UnaryOperator.EXISTS)
        ) {
            sql.append(resolveUnaryOperator(expression.operator()));
            sql.append(" ");
        }

        if (expression.value() instanceof ValueReference valueReference) {
            build(valueReference, sql, context);
        } else if (expression.value() instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else if (expression.value() instanceof SqlFragment sqlFragment) {
            appendInBrackets(sql, sqlFragment, context);
        } else {
            build(value(expression.value()), sql, context);
        }

        if (expression.operator().equals(UnaryOperator.IS_NULL) ||
            expression.operator().equals(UnaryOperator.IS_NOT_NULL)
        ) {
            sql.append(" ");
            sql.append(resolveUnaryOperator(expression.operator()));
        }
    }

    public void build(
        @Nonnull final InExpression expression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (expression.left() instanceof ValueReference valueReference) {
            build(valueReference, sql, context);
        } else if (expression.left() instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else if (expression.left() instanceof SqlFragment sqlFragment) {
            appendInBrackets(sql, sqlFragment, context);
        } else {
            build(value(expression.left()), sql, context);
        }

        sql.append(" IN (");

        if (expression.values() instanceof List<?> valueList) {
            for (int i = 0; i < valueList.size(); i++) {
                build(value(valueList.get(i)), sql, context);

                if (i < valueList.size() - 1) {
                    sql.append(", ");
                }
            }
        } else if (expression.values() instanceof Select valueSelect) {
            build(valueSelect, sql, context);
        } else {
            throw new DatlinSqlPrepareException("Unsupported type of in values '%s'".formatted(expression.values().getClass().getSimpleName()));
        }

        sql.append(")");
    }

    public void build(
        @Nonnull final ValueReference valueReference,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (valueReference.reference() instanceof Long longReference) {
            sql.append(longReference);
        } else if (valueReference.reference() instanceof Double doubleReference) {
            sql.append(doubleReference);
        } else if (valueReference.reference() instanceof Float floatReference) {
            sql.append(floatReference);
        } else if (valueReference.reference() instanceof Integer integerReference) {
            sql.append(integerReference);
        } else if (valueReference.reference() instanceof Short shortReference) {
            sql.append(shortReference);
        } else {
            context.addStatementObjects(valueReference.reference());
            sql.append("?");
        }
    }

    public void build(
        @Nonnull final RawValue value,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append(value.value());
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
    private String resolveComparisonOperator(@Nonnull final BinaryOperator operator) {
        return switch (operator) {
            case EQ -> "=";
            case NEQ -> "<>"; // Lub "!="
            case GT -> ">";
            case GTE -> ">=";
            case LT -> "<";
            case LTE -> "<=";
        };
    }

    @Nonnull
    private String resolveUnaryOperator(@Nonnull final UnaryOperator operator) {
        return switch (operator) {
            case IS_NULL -> "IS NULL";
            case IS_NOT_NULL -> "IS NOT NULL";
            case NOT -> "NOT";
            case EXISTS -> "EXISTS";
        };
    }

    /**
     * Appends a string reference to the {@link StringBuilder} wrapped in the Identifier Quote Character (IQC).
     * <p>
     * This helper method is used to safely add database identifiers (such as table or column names)
     * to a dynamic SQL query. By wrapping the reference in {@link #iqc}, it ensures that the identifier
     * is correctly interpreted by the database engine, even if it is a reserved keyword or
     * contains special characters.
     * </p>
     * * <p><b>Example:</b></p>
     * If {@code iqc} is {@code '"'} and {@code reference} is {@code "user_table"},
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

    /**
     * Wraps the translation of a {@link SqlFragment} in parentheses and appends it to the buffer.
     * <p>
     * This helper method ensures consistent formatting for nested expressions, such as
     * subqueries or grouped logical conditions (e.g., {@code (a AND b)}). It handles the
     * opening bracket, delegates the recursive build process for the fragment,
     * and ensures the closing bracket is appended.
     * </p>
     *
     * @param sql         the buffer to which the bracketed SQL will be appended;
     *                    must not be {@code null}
     * @param sqlFragment the fragment to be built inside the brackets;
     *                    must not be {@code null}
     * @param context     the current build context for parameter tracking and dialect
     *                    settings; must not be {@code null}
     */
    private void appendInBrackets(
        @Nonnull final StringBuilder sql,
        @Nonnull final SqlFragment sqlFragment,
        @Nonnull final BuildContext context
    ) {
        sql.append("(");
        build(sqlFragment, sql, context);
        sql.append(")");
    }
}