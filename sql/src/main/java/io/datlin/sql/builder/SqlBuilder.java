package io.datlin.sql.builder;

import io.datlin.sql.query.Select;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

public interface SqlBuilder {

    void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

}