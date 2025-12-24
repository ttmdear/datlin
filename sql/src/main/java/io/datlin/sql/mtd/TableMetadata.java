package io.datlin.sql.mtd;

import jakarta.annotation.Nonnull;

import java.util.List;

public record TableMetadata(
    @Nonnull String name,
    @Nonnull List<ColumnMetadata> columns
) {
}
