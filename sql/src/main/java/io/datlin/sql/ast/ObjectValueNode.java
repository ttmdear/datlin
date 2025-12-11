package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ObjectValueNode(
    @Nonnull Object value
) implements Node {

}