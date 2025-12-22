package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ColumnReference(
    @Nullable String from,
    @Nonnull String column,
    @Nullable String as
) implements SqlFragment, Aliasable<ColumnReference> {

    @Nonnull
    public ColumnReference from(@Nonnull final String table) {
        return new ColumnReference(table, column, as);
    }

    @Nonnull
    public static ColumnReference column(@Nonnull final String column) {
        return new ColumnReference(null, column, null);
    }

    @Nonnull
    public ColumnReference as(@Nonnull final String as) {
        return new ColumnReference(from, column, as);
    }
}