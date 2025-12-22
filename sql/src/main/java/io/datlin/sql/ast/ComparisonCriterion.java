package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ComparisonCriterion(
    @Nonnull SqlFragment left,
    @Nonnull ComparisonOperator operator,
    @Nonnull SqlFragment right
) implements SqlFragment {

    public ComparisonCriterion eq(
        @Nullable SqlFragment left,
        @Nullable SqlFragment right
    ) {

        if (left == null && right == null) {
            return this;
        }
    }
}
