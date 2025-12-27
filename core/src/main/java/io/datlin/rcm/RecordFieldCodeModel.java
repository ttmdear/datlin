package io.datlin.rcm;

import io.datlin.sql.mtd.ColumnMetadata;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecordFieldCodeModel {

    @Nonnull
    final String name;

    @Nonnull
    final String type;

    @Nonnull
    final Boolean nullable;

    @Nonnull
    final Boolean primaryKey;

    @Nonnull
    final RecordCodeModel record;

    @Nonnull
    final ColumnMetadata metadata;
}