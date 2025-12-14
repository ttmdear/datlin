package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record DeleteNode(
    @Nonnull TableLiteralNode table,
    @Nullable LogicalNode where
) implements Node {

}
