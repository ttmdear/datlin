package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public record RepositoryCodeModel(
    @Nonnull String packageName,
    @Nonnull String recordsPackageName,
    @Nonnull List<RecordCodeModel> records
) {
    public RepositoryCodeModel(
        final @Nonnull String packageName,
        final @Nonnull String recordsPackageName,
        final @Nonnull List<RecordCodeModel> records
    ) {
        this.packageName = packageName;
        this.recordsPackageName = recordsPackageName;
        this.records = unmodifiableList(records);
    }
}