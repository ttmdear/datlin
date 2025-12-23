package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ValueReference(
    @Nonnull Object reference,
    @Nullable String alias
) implements SqlFragment, Aliasable<ValueReference>, ComparisonSupport {

    public static ValueReference value() {

    }

    @Nonnull
    @Override
    public ValueReference as(@Nonnull final String alias) {
        return new ValueReference(reference, alias);
    }
}