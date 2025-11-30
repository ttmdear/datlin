package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

public class RepositoryCodeModel {
    private final @Nonnull List<RecordCodeModel> records;

    public RepositoryCodeModel(
        final @Nonnull List<RecordCodeModel> records
    ) {
        this.records = records;
    }

    public @Nonnull List<RecordCodeModel> getRecords() {
        return Collections.unmodifiableList(records);
    }
}