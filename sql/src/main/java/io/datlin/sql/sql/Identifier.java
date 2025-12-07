package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;

import io.datlin.sql.ast.Node;

@SuppressWarnings("ClassCanBeRecord")
public class Identifier implements Node
{
    @Nonnull final String qualifier;
    @Nonnull final String identifier;

    Identifier(
        @Nonnull final String qualifier,
        @Nonnull final String identifier
    ) {
        this.qualifier = qualifier;
        this.identifier = identifier;
    }

    public static @Nonnull Identifier of(
        @Nonnull final String qualifier,
        @Nonnull final String identifier
    ) {
        return new Identifier(qualifier, identifier);
    }

    public static @Nonnull Identifier of(@Nonnull final String identifier) {
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
