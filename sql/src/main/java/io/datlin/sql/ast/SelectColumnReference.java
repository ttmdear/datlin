package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SelectColumnReference(
    @Nonnull SqlFragment value,
    @Nullable String alias
) implements SqlFragment {
    // update.set(column("user"), "...")
    // update.set(column("user").as(), "...")
    // update.set(column("user"), "...")
    // update.set(column("user"), "...")
    // column("user").from("p").as("...")
    // join(...).as("...")
}