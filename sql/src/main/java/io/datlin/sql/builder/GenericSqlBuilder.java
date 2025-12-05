package io.datlin.sql.builder;

import io.datlin.sql.expression.ColumnExpression;
import io.datlin.sql.expression.ColumnLiteralExpression;
import io.datlin.sql.expression.ConditionsExpression;
import io.datlin.sql.expression.Expression;
import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.expression.TableLiteralExpression;
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
        } else if (expression instanceof ConditionsExpression) {
            build((ConditionsExpression) expression, sql, context);
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

                if (column.value() instanceof ColumnLiteralExpression) {
                    sql.append("\"")
                        .append(column.value())
                        .append("\"");
                } else {
                    sql.append("(");
                    build(column, sql, context);
                    sql.append(")");
                }

                if (i == select.columns().size() - 1) {
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
        final ConditionsExpression where = select.where();
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

            if (i == conditions.conditions().size() - 1) {
                sql.append(" ").append(conditions.operator());
            }
        }
    }
}