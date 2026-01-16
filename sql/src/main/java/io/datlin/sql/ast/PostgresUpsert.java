package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

import static java.util.List.copyOf;

public record PostgresUpsert(
    @Nonnull List<ColumnReference> columns,
    @Nonnull List<List<Object>> values,
    @Nullable TableReference into,

    @Nullable List<ColumnReference> onConflictColumns,
    @Nullable String onConflictConstraint,
    @Nullable Criteria onConflictWhere,

    @Nullable Boolean doNothing,
    @Nullable List<Assignment> doUpdate
) implements SqlFragment {

    @Nonnull
    public static PostgresUpsert upsert() {
        return new PostgresUpsert(List.of(), List.of(), null, null, null, null, null, null);
    }

    @Nonnull
    public PostgresUpsert columns(@Nonnull final ColumnReference... columns) {
        return new PostgresUpsert(List.of(columns), values, into, onConflictColumns, onConflictConstraint,
            onConflictWhere, doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert columns(@Nonnull final List<ColumnReference> columns) {
        return new PostgresUpsert(List.copyOf(columns), values, into, onConflictColumns, onConflictConstraint,
            onConflictWhere, doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert values(@Nonnull final List<List<Object>> values) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert into(@Nonnull final TableReference into) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert onConflict(@Nonnull final ColumnReference... onConflictColumns) {
        return new PostgresUpsert(columns, values, into, List.of(onConflictColumns), onConflictConstraint,
            onConflictWhere, doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert onConflict(@Nonnull final List<ColumnReference> onConflictColumns) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert onConflictConstraint(@Nonnull final String onConflictConstraint) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert onConflictWhere(@Nonnull final Criteria onConflictWhere) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert doNothing(@Nonnull final Boolean doNothing) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }

    @Nonnull
    public PostgresUpsert doUpdate(@Nonnull final List<Assignment> doUpdate) {
        return new PostgresUpsert(columns, values, into, onConflictColumns, onConflictConstraint, onConflictWhere,
            doNothing, doUpdate);
    }
}
