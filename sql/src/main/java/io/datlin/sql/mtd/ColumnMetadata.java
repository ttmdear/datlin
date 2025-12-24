package io.datlin.sql.mtd;

import jakarta.annotation.Nonnull;

public record ColumnMetadata(
    @Nonnull String name,
    @Nonnull Boolean primaryKey,
    @Nonnull String type,
    @Nonnull Boolean nullable
) {
}
