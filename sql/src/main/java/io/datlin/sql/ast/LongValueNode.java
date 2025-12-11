package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record LongValueNode(
    @Nonnull Long value
) implements Node {

}