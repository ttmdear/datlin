package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record TableExpression(
    @Nonnull String table,
    @Nonnull String alias
) implements Expression {

}
