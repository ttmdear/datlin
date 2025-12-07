package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public record UuidValueNode(
    @Nonnull UUID value
) implements Node {

}