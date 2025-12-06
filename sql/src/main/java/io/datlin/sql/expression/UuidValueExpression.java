package io.datlin.sql.expression;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record UuidValueExpression(
    @Nonnull UUID value
) implements Expression {

}