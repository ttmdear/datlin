package io.datlin.rpm;

import io.datlin.sql.metadata.DatabaseMetadata;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryModelFactory {
    public RepositoryModelFactory(
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        // System.out.printf("Creating RepositoryModelFactory with database metadata: %s\n", databaseMetadata);
    }

    public final RepositoryModel create() {
        // ...
        System.out.printf("test");
        return null;
    }
}