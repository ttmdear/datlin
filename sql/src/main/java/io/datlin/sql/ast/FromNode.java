package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.List;

public record FromNode(
    @Nonnull Node source,
    @Nonnull String alias,
    @Nonnull List<JoinNode> joins
) implements Node {

}