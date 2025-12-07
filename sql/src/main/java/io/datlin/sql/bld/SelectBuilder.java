package io.datlin.sql.bld;

import io.datlin.sql.exc.FromNotSetException;
import io.datlin.sql.ast.*;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SelectBuilder {

    @Nonnull
    private final List<ColumnNode> columns = new ArrayList<>();

    @Nullable
    private FromNode from;

    @Nullable
    private LogicalBuilder where;

    @Nonnull
    public SelectBuilder column(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final String alias
    ) {
        this.columns.add(new ColumnNode(new ColumnLiteralNode(table, column), alias));
        return this;
    }

    @Nonnull
    public SelectBuilder from(
        @Nonnull String schema,
        @Nonnull String name,
        @Nonnull String alias
    ) {
        this.from = new FromNode(new TableLiteralNode(schema, name), alias, List.of());
        return this;
    }

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

    @Nonnull
    public SelectNode build() {
        if (this.from == null) {
            throw new FromNotSetException();
        }

        return new SelectNode(
            columns,
            from,
            where != null ? where.build() : null
        );
    }
}
