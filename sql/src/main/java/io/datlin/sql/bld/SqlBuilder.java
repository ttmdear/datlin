package io.datlin.sql.bld;

import io.datlin.sql.ast.SelectNode;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

public interface SqlBuilder {

    void build(
        @Nonnull final SelectNode select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

}