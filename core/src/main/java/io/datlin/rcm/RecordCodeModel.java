package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Getter
@RequiredArgsConstructor
public class RecordCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    @Nonnull
    final TableCodeModel table;

    @Nonnull
    final List<RecordFieldCodeModel> primaryKeys = new ArrayList<>();

    @Nonnull
    final List<RecordFieldCodeModel> fields = new ArrayList<>();

    @Nonnull
    public List<RecordFieldCodeModel> getPrimaryKeys() {
        return unmodifiableList(primaryKeys);
    }

    @Nonnull
    public List<RecordFieldCodeModel> getFields() {
        return unmodifiableList(fields);
    }
}
