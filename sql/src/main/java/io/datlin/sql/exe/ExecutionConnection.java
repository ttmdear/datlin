package io.datlin.sql.exe;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.util.function.Supplier;

public class ExecutionConnection {
    private final @Nonnull Supplier<Connection> connectionSupplier;
    private @Nullable Connection connection;

    public ExecutionConnection(@Nonnull final Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Nonnull
    public synchronized Connection getConnection() {
        if (connection == null) {
            connection = connectionSupplier.get();
        }

        return connection;
    }
}
