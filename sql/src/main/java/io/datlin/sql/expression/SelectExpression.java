package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

import java.util.List;

public record SelectExpression(
    @Nonnull List<ColumnExpression> columns,
    @Nonnull FromExpression from,
    @Nonnull ConditionsExpression where
) implements Expression {

}
