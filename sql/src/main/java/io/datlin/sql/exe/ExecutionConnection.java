package io.datlin.sql.exe;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * A thread-safe wrapper for managing a lazily-initialized JDBC {@link Connection}.
 * <p>
 * This class holds a supplier that provides a database connection only when requested.
 * Once the connection is established, it is cached for the lifetime of this instance
 * to ensure that the same connection is reused for subsequent operations.
 */
public class ExecutionConnection {

    /**
     * The supplier used to provide the connection when it is first requested.
     */
    @Nonnull
    private final Supplier<Connection> connectionSupplier;

    /**
     * The cached database connection. Initialized lazily.
     */
    @Nullable
    private Connection connection;

    /**
     * Constructs a new {@code ExecutionConnection} with the given supplier.
     *
     * @param connectionSupplier a non-null supplier that creates or retrieves a {@link Connection}
     */
    public ExecutionConnection(@Nonnull final Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    /**
     * Provides access to the managed {@link Connection}.
     * <p>
     * If the connection has not been initialized yet, this method invokes the
     * {@code connectionSupplier} to create it. This operation is synchronized
     * to ensure thread safety during the initialization phase.
     *
     * @return the established JDBC connection
     * @throws RuntimeException if the supplier fails to provide a connection
     */
    @Nonnull
    public synchronized Connection getConnection() {
        if (connection == null) {
            connection = connectionSupplier.get();
        }

        return connection;
    }
}
