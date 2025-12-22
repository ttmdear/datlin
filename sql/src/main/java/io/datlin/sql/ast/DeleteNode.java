package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record DeleteNode(
    @Nonnull TableReference table,
    @Nullable Criteria where
) implements SqlFragment {

}
