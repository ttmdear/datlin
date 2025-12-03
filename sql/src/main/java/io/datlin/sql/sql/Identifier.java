package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;

@SuppressWarnings("ClassCanBeRecord")
public class Identifier implements Expression {
    final @Nonnull String qualifier;
    final @Nonnull String identifier;

    Identifier(
        final @Nonnull String qualifier,
        final @Nonnull String identifier
    ) {
        this.qualifier = qualifier;
        this.identifier = identifier;
    }

    public static @Nonnull Identifier of(
        final @Nonnull String qualifier,
        final @Nonnull String identifier
    ) {
        return new Identifier(qualifier, identifier);
    }

    public static @Nonnull Identifier of(final @Nonnull String identifier) {
        final String[] parts = identifier.split("\\.");

        if (parts.length == 1) {
            return new Identifier("", parts[0]);
        } else if (parts.length == 2) {
            return new Identifier(parts[0], parts[1]);
        } else {
            throw new IllegalArgumentException("Incorrect qualifier identifier: " + identifier);
        }
    }
}
