package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record UnaryExpression(
    @Nonnull Object value,
    @Nonnull UnaryOperator operator,
    @Nullable String alias
) implements SqlFragment, Aliasable<UnaryExpression> {

    @Nonnull
    @Override
    public UnaryExpression as(@Nonnull final String alias) {
        return new UnaryExpression(value, operator, alias);
    }

    @Nonnull
    public static UnaryExpression isNull(
        @Nonnull final Object value
    ) {
        return new UnaryExpression(value, UnaryOperator.IS_NULL, null);
    }

    @Nonnull
    public static UnaryExpression isNotNull(
        @Nonnull final Object value
    ) {
        return new UnaryExpression(value, UnaryOperator.IS_NOT_NULL, null);
    }

    @Nonnull
    public static UnaryExpression not(
        @Nonnull final Object value
    ) {
        return new UnaryExpression(value, UnaryOperator.NOT, null);
    }

    @Nonnull
    public static UnaryExpression exists(
        @Nonnull final Object value
    ) {
        return new UnaryExpression(value, UnaryOperator.EXISTS, null);
    }
}