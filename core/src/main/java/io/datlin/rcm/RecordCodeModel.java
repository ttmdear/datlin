package io.datlin.rcm;

import jakarta.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public record RecordCodeModel(
    @Nonnull String table,
    @Nonnull String simpleName,
    @Nonnull String canonicalName,
    @Nonnull String packageName,
    @Nonnull List<RecordFieldCodeModel<?>> fields
) {
    public RecordCodeModel(
        @Nonnull String table,
        @Nonnull String simpleName,
        @Nonnull String canonicalName,
        @Nonnull String packageName,
        @Nonnull List<RecordFieldCodeModel<?>> fields
    ) {
        this.table = table;
        this.simpleName = simpleName;
        this.canonicalName = canonicalName;
        this.packageName = packageName;
        this.fields = unmodifiableList(fields);
    }
}
