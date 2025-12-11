package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.List;

public record RepositoryCodeModel(
    @Nonnull String packageName,
    @Nonnull String recordsPackageName,
    @Nonnull String tablesPackageName,
    @Nonnull String executionsPackageName,
    @Nonnull DatabaseCodeModel database,
    @Nonnull List<TableCodeModel> tables,
    @Nonnull List<RecordCodeModel> records,
    @Nonnull List<ExecutionCodeModel> executions
) {
}