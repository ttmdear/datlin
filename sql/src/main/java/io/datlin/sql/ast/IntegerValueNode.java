package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record IntegerValueNode(
    @Nonnull Integer value
) implements Node {

}