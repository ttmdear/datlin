package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record RecordFieldCodeModel<T>(
    @Nonnull String name,
    @Nonnull String columnName,
    @Nonnull Class<T> type,
    @Nonnull Boolean nullable,
    @Nonnull Boolean primaryKey
) {

}
