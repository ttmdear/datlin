package io.datlin.sql.bld;

import io.datlin.sql.ast.TableReference;
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
    private TableReference table;

    @Nonnull
    private final List<UpdateSetNode> sets = new ArrayList<>();

    @Nullable
    private LogicalBuilder where;

    @Nonnull
    public UpdateBuilder table(
        @Nonnull final String name
    ) {
        table = new TableReference(null, name, null);
        return this;
    }

    @Nonnull
    public UpdateBuilder table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        table = new TableReference(schema, name, null);
        return this;
    }

    @Nonnull
    public UpdateBuilder set(
        @Nonnull final String column,
        @Nullable final Object value
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
