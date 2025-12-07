package io.datlin.sql.builder;

import io.datlin.sql.expression.*;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConditionsExpressionBuilder {

    @Nonnull
    private LogicOperator operator = LogicOperator.AND;

    @Nonnull
    private final List<Object> conditions = new ArrayList<>();

    public ConditionsExpressionBuilder() {

    }

    public ConditionsExpressionBuilder(@Nonnull final LogicOperator operator) {
        this.operator = operator;
    }

    @Nonnull
    public ConditionsExpressionBuilder or(@Nonnull final ConditionsExpressionConfigurer configurer) {
        final ConditionsExpressionBuilder builder = new ConditionsExpressionBuilder(LogicOperator.OR);
        configurer.configure(builder);
        return builder;
    }

    @Nonnull
    public ConditionsExpressionBuilder and(@Nonnull final ConditionsExpressionConfigurer configurer) {
        final ConditionsExpressionBuilder builder = new ConditionsExpressionBuilder(LogicOperator.AND);
        configurer.configure(builder);
        return builder;
    }

    @Nonnull
    public ConditionsExpressionBuilder eq(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final UUID value
    ) {
        conditions.add(
            new BinaryExpression(
                new ColumnLiteralExpression(table, column),
                BinaryOperator.EQ,
                new UuidValueExpression(value)
            )
        );

        return this;
    }

    @Nonnull
    public ConditionsExpression build() {
        final List<Expression> conditions = new ArrayList<>(this.conditions.size());
        for (final Object condition : this.conditions) {
            if (condition instanceof ConditionsExpressionBuilder conditionsExpressionBuilder) {
                conditions.add(conditionsExpressionBuilder.build());
            } else if (condition instanceof Expression expression) {
                conditions.add(expression);
            }
        }
        return new ConditionsExpression(operator, conditions);
    }

    public interface ConditionsExpressionConfigurer {
        void configure(final ConditionsExpressionBuilder builder);
    }
}