package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.List;

public record DatabaseCodeModel(
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull List<ExecutionCodeModel> executions
) {

}
