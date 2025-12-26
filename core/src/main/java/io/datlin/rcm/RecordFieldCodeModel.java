package io.datlin.rcm;

import io.datlin.sql.mtd.ColumnMetadata;
import jakarta.annotation.Nonnull;

public record RecordFieldCodeModel(
    @Nonnull String name,
    @Nonnull String type,
    @Nonnull Boolean nullable,
    @Nonnull Boolean primaryKey,
    @Nonnull ColumnMetadata columnMetadata
) {

}
