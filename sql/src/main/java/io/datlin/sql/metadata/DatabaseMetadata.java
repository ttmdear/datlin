package io.datlin.sql.metadata;

import jakarta.annotation.Nonnull;

import java.util.List;

public record DatabaseMetadata(
    @Nonnull List<Table> tables
) {
    public record Table(
        @Nonnull String name,
        @Nonnull List<Column> columns
    ) {
    }

    public record Column(
        @Nonnull String name,
        @Nonnull Boolean primaryKey,
        @Nonnull String type,
        @Nonnull Boolean nullable
    ) {
    }
}
