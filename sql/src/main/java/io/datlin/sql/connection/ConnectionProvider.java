package io.datlin.sql.connection;

import jakarta.annotation.Nonnull;

import java.sql.Connection;

public interface ConnectionProvider {
    @Nonnull Connection getConnection(final @Nonnull String databaseId);

    @Nonnull Connection getNewConnection(final @Nonnull String databaseId);
}