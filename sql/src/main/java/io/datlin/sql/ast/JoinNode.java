package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record JoinNode(
    @Nonnull Node source,
    @Nonnull String alias,
    @Nonnull Node criterion
) implements Node {

}