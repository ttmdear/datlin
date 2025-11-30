package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public class RecordFieldCodeModel<T> {
    private final @Nonnull String name;
    private final @Nonnull Class<T> type;

    public RecordFieldCodeModel(
        final @Nonnull String name,
        final @Nonnull Class<T> type
    ) {
        this.name = name;
        this.type = type;
    }
}
