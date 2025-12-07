package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record TableLiteralNode(
    @Nullable String schema,
    @Nonnull String name
) implements Node {

}