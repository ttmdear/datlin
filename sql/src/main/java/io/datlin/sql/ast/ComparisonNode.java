package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ComparisonNode(
    @Nonnull Node left,
    @Nonnull ComparisonOperator operator,
    @Nonnull Node right
) implements Node {
}
