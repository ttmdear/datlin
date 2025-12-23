package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record Delete(
    @Nonnull TableReference table,
    @Nullable Criteria where
) implements SqlFragment {

}
