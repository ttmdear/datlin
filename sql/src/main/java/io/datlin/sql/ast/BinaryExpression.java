package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record BinaryExpression(
    @Nonnull Object left,
    @Nonnull BinaryOperator operator,
    @Nonnull Object right,
    @Nullable String alias
) implements SqlFragment, Aliasable<BinaryExpression> {

    @Nonnull
    @Override
    public BinaryExpression as(@Nonnull final String alias) {
        return new BinaryExpression(left, operator, right, alias);
    }

    @Nonnull
    public static BinaryExpression eq(
        @Nonnull final Object left,
        @Nonnull final Object right
    ) {
        return new BinaryExpression(left, BinaryOperator.EQ, right, null);
    }
}