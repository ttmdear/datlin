package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record Comparison(
    @Nonnull Object left,
    @Nonnull ComparisonOperator operator,
    @Nonnull Object right,
    @Nullable String alias
) implements SqlFragment, Aliasable<Comparison> {

    @Nonnull
    @Override
    public Comparison as(@Nonnull final String alias) {
        return new Comparison(left, operator, right, alias);
    }

    @Nonnull
    public static Comparison eq(
        @Nonnull final Object left,
        @Nonnull final Object right
    ) {
        return new Comparison(left, ComparisonOperator.EQ, right, null);
    }
}