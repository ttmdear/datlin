package io.datlin.gec;

import io.datlin.sql.metadata.DatabaseMetadata;
import io.datlin.sql.metadata.DatabaseMetadataFactory;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryCodeGenerator {
    private final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration;

    public RepositoryCodeGenerator(
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        this.xmlRepositoryConfiguration = xmlRepositoryConfiguration;
    }

    public void generate() {
        final DatabaseMetadata databaseMetadata = createDatabaseMetadata();
        System.out.printf("test");
    }

    private @Nonnull DatabaseMetadata createDatabaseMetadata() {
        try (final Connection connection = DriverManager.getConnection(
            xmlRepositoryConfiguration.getConnection().getUrl(),
            xmlRepositoryConfiguration.getConnection().getUsername(),
            xmlRepositoryConfiguration.getConnection().getPassword()
        )) {
            return new DatabaseMetadataFactory(connection).create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
