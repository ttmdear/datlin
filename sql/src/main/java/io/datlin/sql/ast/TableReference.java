package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record TableReference(
    @Nullable String schema,
    @Nonnull String name
) implements SqlFragment {

    @Nonnull
    public static TableReference table(
        @Nonnull final String name
    ) {
        return new TableReference(null, name);
    }

    @Nonnull
    public TableReference schema(
        @Nonnull final String schema
    ) {
        return new TableReference(schema, name);
    }
}