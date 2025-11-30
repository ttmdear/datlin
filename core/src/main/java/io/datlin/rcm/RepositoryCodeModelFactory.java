package io.datlin.rcm;

import io.datlin.sql.metadata.DatabaseMetadata;
import io.datlin.xrc.generated.TableType;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

public class RepositoryCodeModelFactory {
    public RepositoryCodeModel create(
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration,
        final @Nonnull DatabaseMetadata databaseMetadata
    ) {
        final RepositoryCodeModel repositoryCodeModel = new RepositoryCodeModel();
        for (final TableType table : xmlRepositoryConfiguration.getTables()) {
            repositoryCodeModel.addRecord(new RecordCodeModel(table.getName()));
        }
        return repositoryCodeModel;
    }
}