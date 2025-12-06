package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record ColumnReferenceExpression(
    @Nonnull String table,
    @Nonnull String column
) implements Expression {

}