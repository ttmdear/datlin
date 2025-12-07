package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record SelectNode(
    @Nonnull List<ColumnNode> columns,
    @Nonnull FromNode from,
    @Nullable LogicalNode where
) implements Node {

}
