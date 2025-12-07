package io.datlin.sql.builder;

import io.datlin.sql.expression.*;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogicalExpressionBuilder {

    @Nonnull
    private LogicOperator operator = LogicOperator.AND;

    @Nonnull
    private final List<Object> criteria = new ArrayList<>();

    public LogicalExpressionBuilder() {

    }

    public LogicalExpressionBuilder(@Nonnull final LogicOperator operator) {
        this.operator = operator;
    }

    @Nonnull
    public LogicalExpressionBuilder or(@Nonnull final ConditionsExpressionConfigurer configurer) {
        final LogicalExpressionBuilder builder = new LogicalExpressionBuilder(LogicOperator.OR);
        configurer.configure(builder);
        return builder;
    }

    @Nonnull
    public LogicalExpressionBuilder and(@Nonnull final ConditionsExpressionConfigurer configurer) {
        final LogicalExpressionBuilder builder = new LogicalExpressionBuilder(LogicOperator.AND);
        configurer.configure(builder);
        return builder;
    }

    @Nonnull
    public LogicalExpressionBuilder eq(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final UUID value
    ) {
        criteria.add(
            new BinaryExpression(
                new ColumnLiteralExpression(table, column),
                BinaryOperator.EQ,
                new UuidValueExpression(value)
            )
        );

        return this;
    }

    @Nonnull
    public LogicalExpression build() {
        final List<Expression> conditions = new ArrayList<>(this.criteria.size());
        for (final Object condition : this.criteria) {
            if (condition instanceof LogicalExpressionBuilder logicalExpressionBuilder) {
                conditions.add(logicalExpressionBuilder.build());
            } else if (condition instanceof Expression expression) {
                conditions.add(expression);
            }
        }
        return new LogicalExpression(operator, conditions);
    }

    public interface ConditionsExpressionConfigurer {
        void configure(final LogicalExpressionBuilder builder);
    }
}