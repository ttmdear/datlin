package io.datlin.sql.builder;

import jakarta.annotation.Nonnull;

import io.datlin.sql.query.Select;
import io.datlin.sql.sql.BuildContext;
import io.datlin.sql.sql.Expression;

public interface SqlBuilder {
    // @Nonnull String build(
    //     final @Nonnull Expression expression,
    //     final @Nonnull BuildContext context
    // );

    void build(
        final @Nonnull Select select,
        final @Nonnull BuildContext context
    );
}