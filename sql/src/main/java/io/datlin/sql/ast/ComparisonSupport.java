package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public interface ComparisonSupport {

    @Nonnull
    default BinaryExpression eq(@Nonnull final Object right) {
        return new BinaryExpression(this, BinaryOperator.EQ, right, null);
    }
}
