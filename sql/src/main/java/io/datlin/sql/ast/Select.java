package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public record Select(
    @Nonnull List<SqlFragment> columns,
    @Nullable SqlFragment from,
    @Nonnull List<SqlFragment> joins,
    @Nullable Criteria where
) implements SqlFragment {

    @Nonnull
    public static Select select() {
        return new Select(List.of(), null, List.of(), null);
    }

    // columns ---------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select columns(
        @Nonnull final SqlFragment... columns
    ) {
        return new Select(
            Arrays.stream(columns).toList(),
            from,
            joins,
            where
        );
    }

    @Nonnull
    public Select columns(
        @Nonnull final List<SqlFragment> columns
    ) {
        return new Select(columns, from, joins, where);
    }

    // from ------------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select from(@Nonnull final SqlFragment from) {
        return new Select(
            columns,
            from,
            joins,
            where
        );
    }

    // joins -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select joins(@Nonnull final SqlFragment... joins) {
        return new Select(
            columns,
            from,
            Arrays.stream(joins).toList(),
            where
        );
    }

    @Nonnull
    public Select joins(@Nonnull final List<SqlFragment> joins) {
        return new Select(
            columns,
            from,
            joins,
            where
        );
    }

    // where -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select where(@Nonnull final SqlFragment criteria) {
        return new Select(
            columns,
            from,
            joins,
            Criteria.and(criteria)
        );
    }

    @Nonnull
    public Select where(@Nonnull final List<SqlFragment> criteria) {
        return new Select(
            columns,
            from,
            joins,
            Criteria.and(criteria)
        );
    }
}
