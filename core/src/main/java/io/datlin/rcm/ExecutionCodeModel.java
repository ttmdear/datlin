package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record ExecutionCodeModel(
    @Nonnull TableCodeModel tableCodeModel,
    @Nonnull String simpleName,
    @Nonnull String methodName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull String resultSetProcessor,
    @Nonnull RecordCodeModel recordCodeModel
) {

}
