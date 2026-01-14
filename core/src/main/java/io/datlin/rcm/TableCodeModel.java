package io.datlin.rcm;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Nonnull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.datlin.sql.mtd.TableMetadata;

@Getter
@RequiredArgsConstructor
public class TableCodeModel
{

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

    @Nonnull
    final List<RecordCodeModel> records = new ArrayList<>();

    @Nonnull
    final List<ExecutionCodeModel> executions = new ArrayList<>();

    @Nonnull
    public List<TableColumnCodeModel> getColumns() {
        return unmodifiableList(columns);
    }
}