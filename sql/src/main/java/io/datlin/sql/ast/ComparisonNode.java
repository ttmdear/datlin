package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ComparisonNode(
    @Nonnull SqlFragment left,
    @Nonnull ComparisonOperator operator,
    @Nonnull SqlFragment right
) implements SqlFragment {
}
