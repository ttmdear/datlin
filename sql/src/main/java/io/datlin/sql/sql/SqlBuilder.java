package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;

public interface SqlBuilder {
    @Nonnull String build(
        final @Nonnull Expression expression,
        final @Nonnull BuildContext context
    );
}