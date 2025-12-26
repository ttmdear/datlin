package io.datlin.rcm;

import io.datlin.sql.mtd.ColumnMetadata;
import jakarta.annotation.Nonnull;

public record TableColumnCodeModel(
    @Nonnull String name,
    @Nonnull Boolean nullable,
    @Nonnull ColumnMetadata columnMetadata
) {

}
