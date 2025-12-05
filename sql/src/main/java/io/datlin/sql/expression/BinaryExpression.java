package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record BinaryExpression(
    @Nonnull Expression left,
    @Nonnull BinaryOperator operator,
    @Nonnull Expression right
) implements Expression {

}
