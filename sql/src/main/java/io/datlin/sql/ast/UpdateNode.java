package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record UpdateNode(
    @Nonnull TableReference table,
    @Nonnull List<UpdateSetNode> sets,
    @Nullable LogicalNode where
) implements SqlFragment {

}
