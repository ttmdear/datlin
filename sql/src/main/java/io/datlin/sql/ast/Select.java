package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public record Select(
    @Nonnull List<SqlFragment> columns,
    @Nullable SqlFragment from,
    @Nonnull List<SqlFragment> joins,
    @Nullable Criteria where,
    @Nullable String alias
) implements SqlFragment, Aliasable<Select> {

    @Nonnull
    @Override
    public Select as(@Nonnull final String alias) {
        return new Select(columns, from, joins, where, alias);
    }

    @Nonnull
    public static Select select() {
        return new Select(List.of(), null, List.of(), null, null);
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
            where,
            alias
        );
    }

    @Nonnull
    public Select columns(
        @Nonnull final List<SqlFragment> columns
    ) {
        return new Select(columns, from, joins, where, alias);
    }

    // from ------------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select from(@Nonnull final SqlFragment from) {
        return new Select(
            columns,
            from,
            joins,
            where,
            alias
        );
    }

    // joins -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select joins(@Nonnull final SqlFragment... joins) {
        return new Select(
            columns,
            from,
            Arrays.stream(joins).toList(),
            where,
            alias
        );
    }

    @Nonnull
    public Select joins(@Nonnull final List<SqlFragment> joins) {
        return new Select(
            columns,
            from,
            joins,
            where,
            alias
        );
    }

    // where -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public Select where(@Nonnull final SqlFragment criteria) {
        return new Select(
            columns,
            from,
            joins,
            criteria instanceof Criteria ? (Criteria) criteria : Criteria.and(criteria),
            alias
        );
    }

    @Nonnull
    public Select where(@Nonnull final SqlFragment... criteria) {
        return new Select(
            columns,
            from,
            joins,
            Criteria.and(criteria),
            alias
        );
    }

    @Nonnull
    public Select where(@Nonnull final List<SqlFragment> criteria) {
        return new Select(
            columns,
            from,
            joins,
            Criteria.and(criteria),
            alias
        );
    }
}
