package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record ColumnLiteralExpression(
    @Nonnull String value,
    @Nonnull String alias
) implements Expression {

}