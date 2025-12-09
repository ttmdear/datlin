package io.datlin.rcm;

import jakarta.annotation.Nonnull;

public record TableColumnCodeModel(
    @Nonnull String name,
    @Nonnull Boolean nullable
) {

}
