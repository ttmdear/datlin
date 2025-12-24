package io.datlin.rcm;

import io.datlin.sql.mtd.TableMetadata;
import jakarta.annotation.Nonnull;

import java.util.List;

public record TableCodeModel(
    @Nonnull TableMetadata tableMetadata,
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull List<TableColumnCodeModel> columns
) {

}
