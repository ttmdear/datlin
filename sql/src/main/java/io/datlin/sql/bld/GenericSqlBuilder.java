package io.datlin.sql.bld;

import io.datlin.sql.ast.ComparisonNode;
import io.datlin.sql.ast.SelectColumnReference;
import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.ComparisonOperator;
import io.datlin.sql.ast.DeleteNode;
import io.datlin.sql.ast.InsertNode;
import io.datlin.sql.ast.LogicalNode;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.ast.UpdateNode;
import io.datlin.sql.ast.UpdateSetNode;
import io.datlin.sql.ast.ValueNode;
import io.datlin.sql.exc.InsertColumnsNotSetException;
import io.datlin.sql.exc.InsertValuesNumberIsDifferentThenColumnsException;
import io.datlin.sql.exc.UpdateSetIsNotSetException;
import jakarta.annotation.Nonnull;

import java.util.List;

public class GenericSqlBuilder implements SqlBuilder {

    public void build(
        @Nonnull final SqlFragment sqlFragment,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (sqlFragment instanceof Select) {
            build((Select) sqlFragment, sql, context);
        } else if (sqlFragment instanceof LogicalNode) {
            build((LogicalNode) sqlFragment, sql, context);
        } else if (sqlFragment instanceof ComparisonNode) {
            build((ComparisonNode) sqlFragment, sql, context);
        } else if (sqlFragment instanceof ValueNode) {
            build((ValueNode) sqlFragment, sql, context);
        } else if (sqlFragment instanceof SelectColumnReference) {
            build((SelectColumnReference) sqlFragment, sql, context);
        } else if (sqlFragment instanceof ColumnReference) {
            build((ColumnReference) sqlFragment, sql, context);
        }
    }

    @Override
    public void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
//        sql.append("SELECT");
//
//        // columns -----------------------------------------------------------------------------------------------------
//        if (select.columns().isEmpty()) {
//            sql.append(" *");
//        } else {
//            for (int i = 0; i < select.columns().size(); i++) {
//                final SelectColumnReference column = select.columns().get(i);
//
//                sql.append(" ");
//                build(column, sql, context);
//
//                if (i < select.columns().size() - 1) {
//                    sql.append(",");
//                }
//            }
//        }
//
//        // from --------------------------------------------------------------------------------------------------------
//        final SqlFragment fromValue = select.from().source();
//        sql.append(" FROM ");
//
//        if (fromValue instanceof TableReference tableLiteral) {
//            build(tableLiteral, sql, context);
//        } else {
//            sql.append("(");
//            build(fromValue, sql, context);
//            sql.append(")");
//        }
//
//        if (select.from().alias() != null) {
//            sql.append(" AS ").append(select.from().alias());
//        }
//
//        // where -------------------------------------------------------------------------------------------------------
//        if (select.where() != null) {
//            final StringBuilder whereSql = new StringBuilder();
//            build(select.where(), whereSql, context);
//
//            if (!whereSql.isEmpty()) {
//                sql.append(" WHERE ").append(whereSql);
//            }
//        }
    }

    @Override
    public void build(
        @Nonnull final InsertNode insert,
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
        @Nonnull final UpdateNode update,
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
        @Nonnull final DeleteNode delete,
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
        @Nonnull final LogicalNode conditions,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        for (int i = 0; i < conditions.criteria().size(); i++) {
            final SqlFragment condition = conditions.criteria().get(i);
            sql.append("(");
            build(condition, sql, context);
            sql.append(")");

            if (i < conditions.criteria().size() - 1) {
                sql.append(" ").append(conditions.operator());
            }
        }
    }

    public void build(
        @Nonnull final ComparisonNode comparisonNode,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        build(comparisonNode.left(), sql, context);
        sql.append(" ").append(getBinaryOperatorSymbol(comparisonNode.operator())).append(" ");
        build(comparisonNode.right(), sql, context);
    }

    public void build(
        @Nonnull final ValueNode valueNode,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        context.addStatementObjects(valueNode.value());
        sql.append("?");
    }

    public void build(
        @Nonnull final SelectColumnReference selectColumnReference,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (selectColumnReference.value() instanceof ColumnReference columnReference) {
            build(columnReference, sql, context);
        } else {
            sql.append("(");
            build(selectColumnReference.value(), sql, context);
            sql.append(")");
        }

        if (selectColumnReference.alias() != null) {
            sql.append(" AS ").append(selectColumnReference.alias());
        }
    }

    public void build(
        @Nonnull final ColumnReference columnReference,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        if (columnReference.from() != null) {
            sql.append("\"").append(columnReference.from()).append("\"");
            sql.append(".");
        }

        sql.append("\"").append(columnReference.column()).append("\"");
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
    private String getBinaryOperatorSymbol(@Nonnull final ComparisonOperator operator) {
        return switch (operator) {
            case EQ -> "=";
            case NEQ -> "<>"; // Lub "!="
            case GT -> ">";
            case GTE -> ">=";
            case LT -> "<";
            case LTE -> "<=";
        };
    }
}