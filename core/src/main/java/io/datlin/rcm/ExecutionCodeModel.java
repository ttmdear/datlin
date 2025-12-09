package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record ExecutionCodeModel(
    @Nonnull String table,
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull RecordCodeModel record
) {

}
