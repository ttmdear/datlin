package io.datlin.sql.bld;

import io.datlin.sql.exc.FromNotSetException;
import io.datlin.sql.ast.*;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectBuilder {

    @Nonnull
    private final List<Object> columns = new ArrayList<>();

    @Nullable
    private FromNode from;

    @Nullable
    private LogicalBuilder where;

    // columns ---------------------------------------------------------------------------------------------------------

    @Nonnull
    public SelectBuilder columns(@Nonnull final Object... columns) {
        this.columns.addAll(Arrays.stream(columns).toList());
        return this;
    }

    @Nonnull
    public SelectBuilder columns(@Nonnull final List<?> columns) {
        this.columns.addAll(columns);
        return this;
    }

    // from ------------------------------------------------------------------------------------------------------------

    @Nonnull
    public SelectBuilder from(
        @Nonnull String name,
        @Nonnull String alias
    ) {
        this.from = new FromNode(new TableReference(null, name), alias, List.of());
        return this;
    }

    @Nonnull
    public SelectBuilder from(
        @Nullable String schema,
        @Nonnull String name,
        @Nonnull String alias
    ) {
        this.from = new FromNode(new TableReference(schema, name), alias, List.of());
        return this;
    }

    // where -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public SelectBuilder where(
        @Nonnull final LogicalConfigurer configurer
    ) {
        if (this.where == null) {
            this.where = new LogicalBuilder();
        }

        configurer.configure(where);
        return this;
    }

    @Nonnull
    public SelectBuilder whereOr(
        @Nonnull final LogicalConfigurer configurer
    ) {
        if (this.where == null) {
            this.where = new LogicalBuilder(LogicalOperator.OR);
        }

        configurer.configure(where);
        return this;
    }

    // schema, as, schema, value ---------------------------------------------------------------------------------------------

    @Nonnull
    public Select build() {
//        if (this.from == null) {
//            throw new FromNotSetException();
//        }
//
//        return new Select(
//            columns,
//            from,
//            where != null ? where.build() : null
//        );

        return null;
    }
}
