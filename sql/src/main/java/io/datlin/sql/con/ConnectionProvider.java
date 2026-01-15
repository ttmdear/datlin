package io.datlin.sql.con;

import jakarta.annotation.Nonnull;

import java.sql.Connection;

public interface ConnectionProvider {

    @Nonnull
    Connection getDefaultConnection();

    @Nonnull
    Connection getConnection();
}