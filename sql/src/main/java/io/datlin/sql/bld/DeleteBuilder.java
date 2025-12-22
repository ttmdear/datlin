package io.datlin.sql.bld;

import io.datlin.sql.ast.DeleteNode;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import io.datlin.sql.exc.DeleteTableIsNotSetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class DeleteBuilder {

    @Nullable
    private TableReference table;

    @Nullable
    private LogicalBuilder where;

    @Nonnull
    public DeleteBuilder table(
        @Nonnull final String name
    ) {
        table = new TableReference(null, name, null);
        return this;
    }

    @Nonnull
    public DeleteBuilder table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        table = new TableReference(schema, name, null);
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
