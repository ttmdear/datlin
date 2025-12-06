package io.datlin.sql.builder;

import io.datlin.sql.expression.BinaryExpression;
import io.datlin.sql.expression.BinaryOperator;
import io.datlin.sql.expression.ColumnExpression;
import io.datlin.sql.expression.ColumnLiteralExpression;
import io.datlin.sql.expression.ConditionsExpression;
import io.datlin.sql.expression.Expression;
import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.expression.TableLiteralExpression;
import io.datlin.sql.expression.UuidValueExpression;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

import java.nio.file.attribute.UserPrincipalLookupService;

public class GenericSqlBuilder implements SqlBuilder {

    public void build(
        @Nonnull final Expression expression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (expression instanceof SelectExpression) {
            build((SelectExpression) expression, sql, context);
        } else if (expression instanceof ConditionsExpression) {
            build((ConditionsExpression) expression, sql, context);
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
        sql.append("SELECT ");

        // columns -----------------------------------------------------------------------------------------------------
        if (select.columns().isEmpty()) {
            sql.append("*");
        } else {
            for (int i = 0; i < select.columns().size(); i++) {
                final ColumnExpression column = select.columns().get(i);

                if (column.value() instanceof ColumnExpression) {
                    build(column, sql, context);
                } else {
                    sql.append("(");
                    build(column, sql, context);
                    sql.append(")");
                }

                sql.append(" AS ").append(column.alias());

                if (i < select.columns().size() - 1) {
                    sql.append(", ");
                }
            }
        }

        // from --------------------------------------------------------------------------------------------------------
        final Expression fromValue = select.from().value();
        sql.append(" FROM ");

        if (fromValue instanceof TableLiteralExpression) {
            sql.append("\"").append(((TableLiteralExpression) fromValue).value()).append("\"");
        } else {
            sql.append("(");
            build(fromValue, sql, context);
            sql.append(")");
        }

        sql.append(" AS ").append(select.from().alias());

        // where -------------------------------------------------------------------------------------------------------
        if (select.where() != null) {
            sql.append(" WHERE ");
            build(select.where(), sql, context);
        }
    }

    public void build(
        @Nonnull final ConditionsExpression conditions,
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
        sql.append(uuidValueExpression.value());
    }

    public void build(
        @Nonnull final ColumnExpression columnExpression,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (columnExpression.value() instanceof ColumnLiteralExpression columnLiteralExpression) {
            sql.append("\"").append(columnLiteralExpression.table()).append("\"");
            sql.append("\"").append(columnLiteralExpression.column()).append("\"");
        } else {
            sql.append("(");

        }
        sql.append("\"").append(columnExpression.table()).append("\"");
        sql.append(".");
        sql.append("\"").append(columnExpression.column()).append("\"");
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