package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record RawValue(
    @Nonnull Object value,
    @Nullable String alias
) implements SqlFragment, Aliasable<RawValue>, ComparisonSupport {

    @Nonnull
    public static RawValue rawValue(@Nonnull final Object value) {
        return new RawValue(value, null);
    }

    @Nonnull
    @Override
    public RawValue as(@Nonnull final String alias) {
        return new RawValue(value, alias);
    }
}