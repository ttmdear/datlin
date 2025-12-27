package io.datlin.sql.mtd;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ColumnMetadata(
    @Nonnull String name,
    @Nonnull Boolean primaryKey,
    @Nonnull String type,
    @Nonnull Boolean nullable,
    @Nonnull Integer size,
    @Nonnull Integer decimalDigits,
    @Nullable Integer length,
    @Nullable Integer precision,
    @Nullable Integer scale
) {
}
