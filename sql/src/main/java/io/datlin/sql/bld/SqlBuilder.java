package io.datlin.sql.bld;

import io.datlin.sql.ast.InsertNode;
import io.datlin.sql.ast.SelectNode;
import jakarta.annotation.Nonnull;

public interface SqlBuilder {
    void build(
        @Nonnull final SelectNode select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    void build(
        @Nonnull final InsertNode insertNode,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );
}