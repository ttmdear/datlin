package io.datlin.sql.sql;

import static java.util.Objects.requireNonNull;

public final class PostgreSqlBuilder implements SqlBuilder {

    public String build(Expression expression, BuildContext context) {
        StringBuilder sql = new StringBuilder();
        build(expression, sql, context);
        return sql.toString();
    }

    private void build(Expression expression, StringBuilder sql, BuildContext context) {
        if (expression instanceof Select select) {
            buildSelect(select, sql, context);
        } else if (expression instanceof Identifier identifier) {
            buildIdentifier(identifier, sql, context);
        } else if (expression instanceof Where where) {
            buildWhere(where, sql, context);
        } else if (expression instanceof ValueExpression valueExpression) {
            buildValueExpression(valueExpression, sql, context);
        } else if (expression instanceof ValuesExpression valuesExpression) {
            buildValueExpression(valuesExpression, sql, context);
        } else if (expression instanceof Insert insert) {
            buildInsert(insert, sql, context);
        } else if (expression instanceof Update update) {
            buildUpdate(update, sql, context);
        } else if (expression instanceof Delete delete) {
            buildDelete(delete, sql, context);
        } else if (expression instanceof OrderBy orderBy) {
            buildOrderBy(orderBy, sql, context);
        }
    }

    private void buildSelect(Select select, StringBuilder sql, BuildContext context) {
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

    private void buildInsert(Insert insert, StringBuilder sql, BuildContext context) {
        requireNonNull(insert.into, "INTO set not in INSERT.");

        sql.append("INSERT INTO ");
        buildIdentifier(insert.into, sql, context);

        // columns -----------------------------------------------------------------------------------------------------
        if (insert.columns.isEmpty()) {
            throw new BuildSqlException("INSERT columns are empty.");
        }

        sql.append(" (");

        for (int i = 0; i < insert.columns.size(); i++) {
            sql.append("\"").append(insert.columns.get(i)).append("\"");

            if (i < insert.columns.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");

        // values -----------------------------------------------------------------------------------------------------
        if (insert.values.isEmpty()) {
            throw new BuildSqlException("INSERT values are empty.");
        }

        sql.append(" VALUES (");

        for (int i = 0; i < insert.values.size(); i++) {
            sql.append("?");
            context.addStatementObjects(insert.values.get(i));

            if (i < insert.values.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
    }

    private void buildUpdate(Update update, StringBuilder sql, BuildContext context) {
        requireNonNull(update.table, "UPDATE table is not set.");

        sql.append("UPDATE ");
        buildIdentifier(update.table, sql, context);

        // columns -----------------------------------------------------------------------------------------------------
        if (update.columns.isEmpty()) {
            throw new BuildSqlException("INSERT set is empty.");
        }

        sql.append(" SET ");

        for (int i = 0; i < update.columns.size(); i++) {
            sql.append("\"").append(update.columns.get(i)).append("\"").append(" = ?");

            context.addStatementObjects(update.values.get(i));

            if (i < update.columns.size() - 1) {
                sql.append(", ");
            }
        }

        // where -------------------------------------------------------------------------------------------------------
        if (update.where != null) {
            sql.append(" WHERE ");
            build(update.where, sql, context);
        }
    }

    private void buildDelete(Delete delete, StringBuilder sql, BuildContext context) {
        requireNonNull(delete.table, "DELETE table is not set.");

        sql.append("DELETE FROM ");
        buildIdentifier(delete.table, sql, context);

        // where -------------------------------------------------------------------------------------------------------
        if (delete.where != null) {
            sql.append(" WHERE ");
            build(delete.where, sql, context);
        }
    }

    private void buildIdentifier(Identifier identifier, StringBuilder query, BuildContext context) {
        if (!identifier.qualifier.isEmpty() && !identifier.identifier.isEmpty()) {
            query.append("\"%s\".\"%s\"".formatted(identifier.qualifier, identifier.identifier));
        } else if (!identifier.identifier.isEmpty()) {
            query.append("\"%s\"".formatted(identifier.identifier));
        } else {
            throw new RuntimeException("Unknown identifier type: " + identifier);
        }
    }

    private void buildValueExpression(ValueExpression valueExpression, StringBuilder query, BuildContext context) {
        query.append("?");
        context.addStatementObjects(valueExpression.value);
    }

    private void buildValueExpression(ValuesExpression valuesExpression, StringBuilder query, BuildContext context) {
        for (int i = 0; i < valuesExpression.values.size(); i++) {
            Object value = valuesExpression.values.get(i);

            query.append("?");
            context.addStatementObjects(value);

            if (i < valuesExpression.values.size() - 1) {
                query.append(", ");
            }
        }
    }

    private void buildWhere(Where where, StringBuilder query, BuildContext context) {
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

    private void buildOrderBy(OrderBy orderBy, StringBuilder sql, BuildContext context) {
        for (int i = 0; i < orderBy.orderRules.size(); i++) {
            OrderBy.OrderRule rule = orderBy.orderRules.get(i);

            build(rule.expression(), sql, context);
            sql.append(" ").append(rule.direction().name());

            if (i < orderBy.orderRules.size() - 1) {
                sql.append(", ");
            }
        }
    }

    private boolean isWherePartBrackets(Expression expression) {
        return expression instanceof Where ||
            expression instanceof Select ||
            expression instanceof ValuesExpression;
    }
}
