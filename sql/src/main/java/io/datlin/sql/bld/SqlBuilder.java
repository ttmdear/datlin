package io.datlin.sql.bld;

import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.Insert;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.Update;
import jakarta.annotation.Nonnull;

public interface SqlBuilder {

    void build(
        @Nonnull final Insert insert,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    void build(
        @Nonnull final Update update,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

    void build(
        @Nonnull final Delete delete,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    );

}