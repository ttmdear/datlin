package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record RecordFieldCodeModel<T>(
    @Nonnull String name,
    @Nonnull Class<T> type,
    @Nonnull Boolean nullable
) {

}
