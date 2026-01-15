package io.datlin.rcm;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nonnull;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.datlin.sql.mtd.TableMetadata;

@Getter
@RequiredArgsConstructor
public class TableCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    // @Nonnull
    // final String tableReferenceField;

    @Nonnull
    final TableMetadata metadata;

    @Nonnull
    final DatabaseCodeModel database;

    @Nonnull
    final List<TableColumnCodeModel> columns = new ArrayList<>();

    @Nullable
    RecordCodeModel record = null;

    @Nullable
    TableExecutionCodeModel execution = null;

    @Nonnull
    public List<TableColumnCodeModel> getColumns() {
        return unmodifiableList(columns);
    }

    @Nonnull
    public RecordCodeModel getRecord() {
        if (record == null) {
            throw new IllegalStateException("record is not set for table.");
        }

        return record;
    }

    @Nonnull
    public TableExecutionCodeModel getExecution() {
        if (execution == null) {
            throw new IllegalStateException("execution is not set for table.");
        }

        return execution;
    }
}