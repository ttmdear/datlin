package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public record TableCodeModel(
    @Nonnull String table,
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull List<TableColumnCodeModel> columns
) {

}
