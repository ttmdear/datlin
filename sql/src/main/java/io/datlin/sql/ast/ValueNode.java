package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

public record ValueNode(
    @Nonnull Object value
) implements SqlFragment {

}