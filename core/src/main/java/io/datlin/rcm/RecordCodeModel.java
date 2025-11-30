package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public class RecordCodeModel {
    private final @Nonnull String name;

    public RecordCodeModel(
        final @Nonnull String name
    ) {
        this.name = name;
    }
}
