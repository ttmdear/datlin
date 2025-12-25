package io.datlin.rcm;

import io.datlin.sql.mtd.TableMetadata;
import jakarta.annotation.Nonnull;

import java.util.List;

public record TableCodeModel(
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull String field,
    @Nonnull TableMetadata tableMetadata,
    @Nonnull List<TableColumnCodeModel> columnCodeModels
) {

}
