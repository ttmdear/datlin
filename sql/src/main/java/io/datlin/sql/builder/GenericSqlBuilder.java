package io.datlin.sql.builder;

import io.datlin.sql.expression.BinaryExpression;
import io.datlin.sql.expression.BinaryOperator;
import io.datlin.sql.expression.ColumnExpression;
import io.datlin.sql.expression.ColumnLiteralExpression;
import io.datlin.sql.expression.LogicalExpression;
import io.datlin.sql.expression.Expression;
import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.expression.TableLiteralExpression;
import io.datlin.sql.expression.UuidValueExpression;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

public class GenericSqlBuilder implements SqlBuilder {

    public void build(
        @Nonnull final Expression expression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (expression instanceof SelectExpression) {
            build((SelectExpression) expression, sql, context);
        } else if (expression instanceof LogicalExpression) {
            build((LogicalExpression) expression, sql, context);
        } else if (expression instanceof BinaryExpression) {
            build((BinaryExpression) expression, sql, context);
        } else if (expression instanceof UuidValueExpression) {
            build((UuidValueExpression) expression, sql, context);
        } else if (expression instanceof ColumnExpression) {
            build((ColumnExpression) expression, sql, context);
        } else if (expression instanceof ColumnLiteralExpression) {
            build((ColumnLiteralExpression) expression, sql, context);
        }
    }

    @Override
    public void build(
        @Nonnull final SelectExpression select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("SELECT");

        // columns -----------------------------------------------------------------------------------------------------
        if (select.columns().isEmpty()) {
            sql.append(" *");
        } else {
            for (int i = 0; i < select.columns().size(); i++) {
                final ColumnExpression column = select.columns().get(i);

                sql.append(" ");
                build(column, sql, context);

                if (i < select.columns().size() - 1) {
                    sql.append(",");
                }
            }
        }

        // from --------------------------------------------------------------------------------------------------------
        final Expression fromValue = select.from().value();
        sql.append(" FROM ");

        if (fromValue instanceof TableLiteralExpression tableLiteralExpression) {
            sql.append("\"").append(tableLiteralExpression.schema()).append("\"");
            sql.append(".\"").append(tableLiteralExpression.name()).append("\"");
        } else {
            sql.append("(");
            build(fromValue, sql, context);
            sql.append(")");
        }

        sql.append(" AS ").append(select.from().alias());

        // where -------------------------------------------------------------------------------------------------------
        if (select.where() != null) {
            final StringBuilder whereSql = new StringBuilder();
            build(select.where(), whereSql, context);

            if (!whereSql.isEmpty()) {
                sql.append(" WHERE ").append(whereSql);
            }
        }
    }

    public void build(
        @Nonnull final LogicalExpression conditions,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        for (int i = 0; i < conditions.conditions().size(); i++) {
            final Expression condition = conditions.conditions().get(i);
            sql.append("(");
            build(condition, sql, context);
            sql.append(")");

            if (i < conditions.conditions().size() - 1) {
                sql.append(" ").append(conditions.operator());
            }
        }
    }

    public void build(
        @Nonnull final BinaryExpression binaryExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        build(binaryExpression.left(), sql, context);
        sql.append(" ").append(getBinaryOperatorSymbol(binaryExpression.operator())).append(" ");
        build(binaryExpression.right(), sql, context);
    }

    public void build(
        @Nonnull final UuidValueExpression uuidValueExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        context.addStatementObjects(uuidValueExpression.value());
        sql.append("?");
    }

    public void build(
        @Nonnull final ColumnExpression columnExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (columnExpression.value() instanceof ColumnLiteralExpression columnLiteralExpression) {
            sql.append("\"").append(columnLiteralExpression.table()).append("\"");
            sql.append(".");
            sql.append("\"").append(columnLiteralExpression.column()).append("\"");
        } else {
            sql.append("(");
            build(columnExpression.value(), sql, context);
            sql.append(")");
        }

        sql.append(" AS ").append(columnExpression.alias());
    }

    public void build(
        @Nonnull final ColumnLiteralExpression columnLiteralExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("\"").append(columnLiteralExpression.table()).append("\"");
        sql.append(".");
        sql.append("\"").append(columnLiteralExpression.column()).append("\"");
    }

    @Nonnull
    private String getBinaryOperatorSymbol(@Nonnull final BinaryOperator operator) {
        return switch (operator) {
            case EQ -> "=";
            case NEQ -> "<>"; // Lub "!="
            case GT -> ">";
            case GTE -> ">=";
            case LT -> "<";
            case LTE -> "<=";

            case AND -> "AND";
            case OR -> "OR";

            case ADD -> "+";
            case SUB -> "-";
            case MUL -> "*";
            case DIV -> "/";

            case LIKE -> "LIKE";
            case IN -> "IN";
            case BETWEEN -> "BETWEEN";
        };
    }
}