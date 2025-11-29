package io.datlin.rcm;

import io.datlin.sql.metadata.DatabaseMetadata;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

public class RepositoryCodeModelFactory {
    public RepositoryCodeModel create(
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration,
        final @Nonnull DatabaseMetadata databaseMetadata
    ) {
        System.out.printf("test");
        return null;
    }
}