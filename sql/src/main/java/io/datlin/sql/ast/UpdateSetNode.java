package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record UpdateSetNode(
    @Nonnull String column,
    @Nullable Object value
) implements SqlFragment {

}
