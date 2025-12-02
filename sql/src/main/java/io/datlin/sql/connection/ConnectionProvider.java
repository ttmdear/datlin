package io.datlin.sql.connection;

import jakarta.annotation.Nonnull;

import java.sql.Connection;

@SuppressWarnings("unused")
public interface ConnectionProvider {
    // @formatter:off
    @Nonnull Connection getDefaultConnection();
    @Nonnull Connection getConnection();
    // @formatter:on
}