package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record InExpression(
    @Nonnull Object left,
    @Nonnull Object values,
    @Nullable String alias
) implements SqlFragment, Aliasable<InExpression> {

    @Nonnull
    @Override
    public InExpression as(@Nonnull final String alias) {
        return new InExpression(left, values, alias);
    }

    @Nonnull
    public static InExpression in(
        @Nonnull final Object left,
        @Nonnull final Object values
    ) {
        return new InExpression(left, values, null);
    }
}