package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

import java.util.List;

public record LogicalExpression(
    @Nonnull LogicOperator operator,
    @Nonnull List<Expression> conditions
) implements Expression {

}