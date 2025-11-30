package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositoryCodeModel {
    private final @Nonnull List<RecordCodeModel> records = new ArrayList<>();

    public void addRecord(final @Nonnull RecordCodeModel record) {
        this.records.add(record);
    }

    public @Nonnull List<RecordCodeModel> getRecords() {
        return Collections.unmodifiableList(records);
    }
}