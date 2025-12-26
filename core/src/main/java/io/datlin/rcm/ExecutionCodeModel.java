package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record ExecutionCodeModel(
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull String methodName,
    @Nonnull String resultSetProcessor,
    @Nonnull TableCodeModel tableCodeModel,
    @Nonnull RecordCodeModel recordCodeModel
) {

}
