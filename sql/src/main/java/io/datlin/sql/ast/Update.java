package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record Update(
    @Nonnull TableReference table,
    @Nonnull List<UpdateSetNode> sets,
    @Nullable Criteria where
) implements SqlFragment {

}
