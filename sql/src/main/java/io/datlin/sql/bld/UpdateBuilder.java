package io.datlin.sql.bld;

import io.datlin.sql.ast.TableLiteralNode;
import io.datlin.sql.ast.UpdateNode;
import io.datlin.sql.ast.UpdateSetNode;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import io.datlin.sql.exc.UpdateTableIsNotSetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UpdateBuilder {

    @Nullable
    private TableLiteralNode table;

    @Nonnull
    private final List<UpdateSetNode> sets = new ArrayList<>();

    @Nullable
    private LogicalBuilder where;

    @Nonnull
    public UpdateBuilder table(
        @Nonnull final String name
    ) {
        table = new TableLiteralNode(null, name);
        return this;
    }

    @Nonnull
    public UpdateBuilder table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        table = new TableLiteralNode(schema, name);
        return this;
    }

    @Nonnull
    public UpdateBuilder set(
        @Nonnull final String column,
        @Nonnull final Object value
    ) {
        sets.add(new UpdateSetNode(column, value));
        return this;
    }

    @Nonnull
    public UpdateBuilder where(
        @Nonnull final LogicalConfigurer configurer
    ) {
        if (this.where == null) {
            this.where = new LogicalBuilder();
        }

        configurer.configure(where);
        return this;
    }

    @Nonnull
    public UpdateNode build() {
        if (this.table == null) {
            throw new UpdateTableIsNotSetException();
        }

        return new UpdateNode(
            table,
            sets,
            where != null ? where.build() : null
        );
    }
}
