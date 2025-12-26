package io.datlin.rcm;

import io.datlin.sql.mtd.TableMetadata;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Getter
@RequiredArgsConstructor
public class TableCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    @Nonnull
    final TableMetadata metadata;

    @Nonnull
    final DatabaseCodeModel database;

    @Nonnull
    final List<TableColumnCodeModel> columns = new ArrayList<>();

    @Nonnull
    public List<TableColumnCodeModel> getColumns() {
        return unmodifiableList(columns);
    }
}