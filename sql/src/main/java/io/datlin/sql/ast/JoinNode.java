package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record JoinNode(
    @Nonnull SqlFragment source,
    @Nonnull String alias,
    @Nonnull SqlFragment criterion
) implements SqlFragment {

}