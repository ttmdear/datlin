package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public record FromNode(
    @Nonnull SqlFragment source,
    @Nullable String alias,
    @Nonnull List<JoinNode> joins
) implements SqlFragment {

}