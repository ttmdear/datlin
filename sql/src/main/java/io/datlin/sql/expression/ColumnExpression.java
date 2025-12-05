package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record ColumnExpression(
    @Nonnull Expression value,
    @Nonnull String alias
) implements Expression {

}