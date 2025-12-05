package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

public record TableLiteralExpression(
    @Nonnull String value
) implements Expression {

}