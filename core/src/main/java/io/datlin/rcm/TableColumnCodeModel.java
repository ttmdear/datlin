package io.datlin.rcm;

import io.datlin.sql.mtd.ColumnMetadata;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TableColumnCodeModel {

    @Nonnull
    final String name;

    @Nonnull
    final Boolean nullable;

    @Nonnull
    final ColumnMetadata metadata;

    @Nonnull
    final TableCodeModel table;
}