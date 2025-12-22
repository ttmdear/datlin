package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public record Select(
    @Nonnull List<SqlFragment> columns,
    @Nonnull List<SqlFragment> from,
    @Nonnull List<SqlFragment> where
) implements SqlFragment {

    @Nonnull
    public static Select select() {
        return new Select(List.of(), List.of(), List.of());
    }

    // columns ---------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select columns(
        @Nonnull final SqlFragment... columns
    ) {
        return new Select(
            Arrays.stream(columns).toList(),
            from,
            where
        );
    }

    @Nonnull
    public Select columns(
        @Nonnull final List<SqlFragment> columns
    ) {
        return new Select(columns, from, where);
    }

    // from ------------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select from(@Nonnull final SqlFragment... from) {
        return new Select(columns, Arrays.stream(from).toList(), where);
    }

    @Nonnull
    public Select from(@Nonnull final List<SqlFragment> from) {
        return new Select(columns, from, where);
    }

    // where ------------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select where(@Nonnull final SqlFragment... where) {
        return new Select(columns, from, Arrays.stream(where).toList());
    }

    @Nonnull
    public Select where(@Nonnull final List<SqlFragment> where) {
        return new Select(columns, from, where);
    }
}
