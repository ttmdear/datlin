package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ColumnLiteralNode(
    @Nonnull String table,
    @Nonnull String column
) implements Node {

}