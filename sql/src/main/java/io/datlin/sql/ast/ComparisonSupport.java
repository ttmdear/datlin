package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public interface ComparisonSupport {

    @Nonnull
    default Comparison eq(@Nonnull final Object right) {
        return new Comparison(this, ComparisonOperator.EQ, right, null);
    }
}
