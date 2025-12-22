package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record StringValueNode(
    @Nonnull String value
) implements SqlFragment {

}