package io.datlin.sql.clause;

import io.datlin.sql.sql.Expression;
import jakarta.annotation.Nonnull;

public class From {
    final Expression value;
    final String alias;

    From(
        final @Nonnull Expression value,
        final @Nonnull String alias
    ) {
        this.value = value;
        this.alias = alias;
    }

    public static From build(
        final @Nonnull Expression value,
        final @Nonnull String alias
    ) {
        return new From(value, alias);
    }
}