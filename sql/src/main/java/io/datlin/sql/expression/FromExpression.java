package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

import java.util.List;

public record FromExpression(
    @Nonnull Expression value,
    @Nonnull String alias,
    @Nonnull List<JoinExpression> joins
) implements Expression {

}