package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record StringValueNode(
    @Nonnull String value
) implements Node {

}