package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.List;

public class RecordCodeModel {
    private final @Nonnull String table;
    private final @Nonnull String simpleName;
    private final @Nonnull String canonicalName;
    private final @Nonnull String packageName;
    private final @Nonnull List<RecordFieldCodeModel<?>> fields;

    public RecordCodeModel(
        final @Nonnull String table,
        final @Nonnull String simpleName,
        final @Nonnull String canonicalName,
        final @Nonnull String packageName,
        final @Nonnull List<RecordFieldCodeModel<?>> fields
    ) {
        this.table = table;
        this.simpleName = simpleName;
        this.canonicalName = canonicalName;
        this.packageName = packageName;
        this.fields = fields;
    }
}
