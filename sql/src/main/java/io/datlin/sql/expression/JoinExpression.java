package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record JoinExpression(
    @Nonnull Expression table,
    @Nonnull String alias,
    @Nonnull Expression condition
) implements Expression {

}