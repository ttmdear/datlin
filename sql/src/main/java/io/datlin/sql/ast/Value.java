package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record Value(
    @Nonnull Object value,
    @Nullable String alias
) implements SqlFragment, Aliasable<Value> {

    @Nonnull
    @Override
    public Value as(@Nonnull final String alias) {
        return new Value(value, alias);
    }
}