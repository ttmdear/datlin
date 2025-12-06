package io.datlin.sql.expression;

import io.datlin.sql.builder.SelectExpressionBuilder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record SelectExpression(
    @Nonnull List<ColumnExpression> columns,
    @Nonnull FromExpression from,
    @Nullable Expression where
) implements Expression {

    @Nonnull
    public static SelectExpressionBuilder builder() {
        return new SelectExpressionBuilder();
    }

}
