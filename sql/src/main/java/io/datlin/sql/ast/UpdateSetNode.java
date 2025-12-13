package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record UpdateSetNode(
    @Nonnull String column,
    @Nonnull Object value
) implements Node {

}
