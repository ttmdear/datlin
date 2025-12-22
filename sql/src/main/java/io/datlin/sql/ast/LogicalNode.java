package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.List;

public record LogicalNode(
    @Nonnull LogicalOperator operator,
    @Nonnull List<SqlFragment> criteria
) implements SqlFragment {
}