package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record TableReference(
    @Nullable String schema,
    @Nonnull String name,
    @Nullable String alias
) implements SqlFragment, Aliasable<TableReference> {

    @Nonnull
    public static TableReference table(
        @Nonnull final String name
    ) {
        return new TableReference(null, name, null);
    }

    @Nonnull
    public TableReference schema(
        @Nonnull final String schema
    ) {
        return new TableReference(schema, name, alias);
    }

    @Nonnull
    @Override
    public TableReference as(@Nonnull final String alias) {
        return new TableReference(schema, name, alias);
    }
}