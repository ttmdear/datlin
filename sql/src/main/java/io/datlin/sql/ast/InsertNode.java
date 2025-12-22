package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;

import java.util.List;

public record InsertNode(
    @Nonnull List<String> columns,
    @Nonnull List<List<Object>> values,
    @Nonnull TableReference into
) implements SqlFragment {

}
