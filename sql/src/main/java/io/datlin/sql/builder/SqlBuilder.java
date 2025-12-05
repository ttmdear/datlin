package io.datlin.sql.builder;

import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

public interface SqlBuilder {

    void build(
        @Nonnull final SelectExpression select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

}