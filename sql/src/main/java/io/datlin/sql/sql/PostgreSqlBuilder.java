package io.datlin.sql.sql;

import io.datlin.sql.clause.OrderBy;
import io.datlin.sql.clause.Where;
import io.datlin.sql.query.Delete;
import io.datlin.sql.query.Insert;
import io.datlin.sql.query.Select;
import io.datlin.sql.query.Update;
import jakarta.annotation.Nonnull;

public class PostgreSqlBuilder implements SqlBuilder {
    public @Nonnull String build(
        final @Nonnull Expression expression,
        final @Nonnull BuildContext context
    ) {
        final StringBuilder stringBuilder = new StringBuilder();
        build(expression, stringBuilder, context);
        return stringBuilder.toString();
    }

    private void build(
        final @Nonnull Expression expression,
        final @Nonnull StringBuilder stringBuilder,
        final @Nonnull BuildContext context
    ) {
        if (expression instanceof Select select) {
            buildSelect(select, stringBuilder, context);
        } else if (expression instanceof Identifier identifier) {
            buildIdentifier(identifier, stringBuilder, context);
        } else if (expression instanceof Where where) {
            buildWhere(where, stringBuilder, context);
        } else if (expression instanceof ValueExpression valueExpression) {
            buildValueExpression(valueExpression, stringBuilder, context);
        } else if (expression instanceof ValuesExpression valuesExpression) {
            buildValueExpression(valuesExpression, stringBuilder, context);
        } else if (expression instanceof Insert insert) {
            buildInsert(insert, stringBuilder, context);
        } else if (expression instanceof Update update) {
            buildUpdate(update, stringBuilder, context);
        } else if (expression instanceof Delete delete) {
            buildDelete(delete, stringBuilder, context);
        } else if (expression instanceof OrderBy orderBy) {
            buildOrderBy(orderBy, stringBuilder, context);
        }
    }

    private void buildSelect(
        final @Nonnull Select select,
        final @Nonnull StringBuilder sql,
        final @Nonnull BuildContext context
    ) {
//        // columns -----------------------------------------------------------------------------------------------------
//        sql.append("SELECT ");
//
//        for (int i = 0; i < select.columns.size(); i++) {
//            if (i > 0) sql.append(", ");
//
//            Column column = select.columns.get(i);
//
//            build(column.value, sql, context);
//
//            if (!column.alias.isEmpty()) {
//                sql.append(" AS ").append(column.alias);
//            }
//        }
//
//        // from --------------------------------------------------------------------------------------------------------
//        if (select.from == null) {
//            throw new BuildSqlException("From is not set");
//        }
//
//        sql.append(" FROM ");
//        build(select.from.value, sql, context);
//
//        if (!select.from.alias.isEmpty()) {
//            sql.append(" AS ").append(select.from.alias);
//        }
//
//        // where -------------------------------------------------------------------------------------------------------
//        if (select.where != null) {
//            sql.append(" WHERE ");
//
//            build(select.where, sql, context);
//        }
//
//        // order by ----------------------------------------------------------------------------------------------------
//        if (select.orderBy != null) {
//            sql.append(" ORDER BY ");
//
//            buildOrderBy(select.orderBy, sql, context);
//        }
    }

    private void buildInsert(
        final @Nonnull Insert insert,
        final @Nonnull StringBuilder stringBuilder,
        final @Nonnull BuildContext context
    ) {
        stringBuilder.append("INSERT INTO ");
        buildIdentifier(insert.into, stringBuilder, context);

        // columns -----------------------------------------------------------------------------------------------------
        if (insert.columns.isEmpty()) {
            throw new BuildSqlException("INSERT columns are empty.");
        }

        stringBuilder.append(" (");

        for (int i = 0; i < insert.columns.size(); i++) {
            stringBuilder.append("\"").append(insert.columns.get(i)).append("\"");

            if (i < insert.columns.size() - 1) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(")");

        // values -----------------------------------------------------------------------------------------------------
        if (insert.values.isEmpty()) {
            throw new BuildSqlException("INSERT values are empty.");
        }

        stringBuilder.append(" VALUES (");

        for (int i = 0; i < insert.values.size(); i++) {
            stringBuilder.append("?");
            context.addStatementObjects(insert.values.get(i));

            if (i < insert.values.size() - 1) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(")");
    }

    private void buildUpdate(
        final @Nonnull Update update,
        final @Nonnull StringBuilder stringBuilder,
        final @Nonnull BuildContext context
    ) {
        stringBuilder.append("UPDATE ");
        buildIdentifier(update.table, stringBuilder, context);

        // columns -----------------------------------------------------------------------------------------------------
        if (update.columns.isEmpty()) {
            throw new BuildSqlException("INSERT set is empty.");
        }

        stringBuilder.append(" SET ");

        for (int i = 0; i < update.columns.size(); i++) {
            stringBuilder.append("\"").append(update.columns.get(i)).append("\"").append(" = ?");

            context.addStatementObjects(update.values.get(i));

            if (i < update.columns.size() - 1) {
                stringBuilder.append(", ");
            }
        }

        // where -------------------------------------------------------------------------------------------------------
        if (update.where != null) {
            stringBuilder.append(" WHERE ");
            build(update.where, stringBuilder, context);
        }
    }

    private void buildDelete(
        final @Nonnull Delete delete,
        final @Nonnull StringBuilder sql,
        final @Nonnull BuildContext context
    ) {
        sql.append("DELETE FROM ");
        buildIdentifier(delete.table, sql, context);

        // where -------------------------------------------------------------------------------------------------------
        if (delete.where != null) {
            sql.append(" WHERE ");
            build(delete.where, sql, context);
        }
    }

    private void buildIdentifier(
        final @Nonnull Identifier identifier,
        final @Nonnull StringBuilder query,
        final @Nonnull BuildContext context
    ) {
        if (!identifier.qualifier.isEmpty() && !identifier.identifier.isEmpty()) {
            query.append("\"%s\".\"%s\"".formatted(identifier.qualifier, identifier.identifier));
        } else if (!identifier.identifier.isEmpty()) {
            query.append("\"%s\"".formatted(identifier.identifier));
        } else {
            throw new RuntimeException("Unknown identifier type: " + identifier);
        }
    }

    private void buildValueExpression(
        final @Nonnull ValueExpression valueExpression,
        final @Nonnull StringBuilder query,
        final @Nonnull BuildContext context
    ) {
        query.append("?");
        context.addStatementObjects(valueExpression.value);
    }

    private void buildValueExpression(
        final @Nonnull ValuesExpression valuesExpression,
        final @Nonnull StringBuilder query,
        final @Nonnull BuildContext context
    ) {
        for (int i = 0; i < valuesExpression.values.size(); i++) {
            Object value = valuesExpression.values.get(i);

            query.append("?");
            context.addStatementObjects(value);

            if (i < valuesExpression.values.size() - 1) {
                query.append(", ");
            }
        }
    }

    private void buildWhere(
        final @Nonnull Where where,
        final @Nonnull StringBuilder query,
        final @Nonnull BuildContext context
    ) {
        for (int i = 0; i < where.conditions.size(); i++) {
            Where.Condition condition = where.conditions.get(i);

            if (isWherePartBrackets(condition.left())) {
                query.append(" (");
                build(condition.left(), query, context);
                query.append(")");
            } else {
                build(condition.left(), query, context);
            }

            if (condition.operator().equals(RelationOperator.EQUAL)) {
                query.append(" = ");
            } else if (condition.operator().equals(RelationOperator.GT)) {
                query.append(" > ");
            } else if (condition.operator().equals(RelationOperator.GTE)) {
                query.append(" >= ");
            } else if (condition.operator().equals(RelationOperator.LT)) {
                query.append(" < ");
            } else if (condition.operator().equals(RelationOperator.LTE)) {
                query.append(" <= ");
            } else if (condition.operator().equals(RelationOperator.IN)) {
                query.append(" IN ");
            }

            if (isWherePartBrackets(condition.right())) {
                query.append(" (");
                build(condition.right(), query, context);
                query.append(")");
            } else {
                build(condition.right(), query, context);
            }

            if (i < where.conditions.size() - 1) {
                query.append(" ").append(where.operator.name()).append(" ");
            }
        }
    }

    private void buildOrderBy(
        final @Nonnull OrderBy orderBy,
        final @Nonnull StringBuilder sql,
        final @Nonnull BuildContext context
    ) {
        for (int i = 0; i < orderBy.orderRules.size(); i++) {
            OrderBy.OrderRule rule = orderBy.orderRules.get(i);

            build(rule.expression(), sql, context);
            sql.append(" ").append(rule.direction().name());

            if (i < orderBy.orderRules.size() - 1) {
                sql.append(", ");
            }
        }
    }

    private boolean isWherePartBrackets(
        final @Nonnull Expression expression
    ) {
        return expression instanceof Where ||
            expression instanceof Select ||
            expression instanceof ValuesExpression;
    }
}
