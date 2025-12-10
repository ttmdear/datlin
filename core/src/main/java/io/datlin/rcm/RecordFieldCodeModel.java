package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record RecordFieldCodeModel(
    @Nonnull String name,
    @Nonnull String columnName,
    @Nonnull String type,
    @Nonnull Boolean nullable,
    @Nonnull Boolean primaryKey
) {

}
