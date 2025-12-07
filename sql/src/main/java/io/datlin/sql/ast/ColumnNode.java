package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ColumnNode(
    @Nonnull Node value,
    @Nonnull String alias
) implements Node {

}