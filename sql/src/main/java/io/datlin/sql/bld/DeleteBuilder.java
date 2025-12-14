package io.datlin.sql.bld;

import io.datlin.sql.ast.DeleteNode;
import io.datlin.sql.ast.TableLiteralNode;
import io.datlin.sql.ast.UpdateNode;
import io.datlin.sql.ast.UpdateSetNode;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import io.datlin.sql.exc.DeleteTableIsNotSetException;
import io.datlin.sql.exc.UpdateTableIsNotSetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteBuilder {

    @Nullable
    private TableLiteralNode table;

    @Nullable
    private LogicalBuilder where;

    @Nonnull
    public DeleteBuilder table(
        @Nonnull final String name
    ) {
        table = new TableLiteralNode(null, name);
        return this;
    }

    @Nonnull
    public DeleteBuilder table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        table = new TableLiteralNode(schema, name);
        return this;
    }

    @Nonnull
    public DeleteBuilder where(
        @Nonnull final LogicalConfigurer configurer
    ) {
        if (this.where == null) {
            this.where = new LogicalBuilder();
        }

        configurer.configure(where);
        return this;
    }

    @Nonnull
    public DeleteNode build() {
        if (this.table == null) {
            throw new DeleteTableIsNotSetException();
        }

        return new DeleteNode(
            table,
            where != null ? where.build() : null
        );
    }
}
