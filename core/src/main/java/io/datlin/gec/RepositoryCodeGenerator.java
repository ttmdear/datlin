package io.datlin.gec;

import io.datlin.frm.TemplateProcessor;
import io.datlin.rcm.RecordCodeModel;
import io.datlin.rcm.RepositoryCodeModel;
import io.datlin.rcm.RepositoryCodeModelFactory;
import io.datlin.sql.mtd.DatabaseMetadata;
import io.datlin.sql.mtd.DatabaseMetadataFactory;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class RepositoryCodeGenerator {
    private final @Nonnull String xmlRepositoryConfigurationPath;
    private final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration;
    private final @Nonnull TemplateProcessor templateProcessor;

    public RepositoryCodeGenerator(
        final @Nonnull String xmlRepositoryConfigurationPath,
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration,
        final @Nonnull TemplateProcessor templateProcessor
    ) {
        this.xmlRepositoryConfigurationPath = xmlRepositoryConfigurationPath;
        this.xmlRepositoryConfiguration = xmlRepositoryConfiguration;
        this.templateProcessor = templateProcessor;
    }

    public void generate() {
        final DatabaseMetadata databaseMetadata = createDatabaseMetadata();
        final RepositoryCodeModel repositoryCodeModel = new RepositoryCodeModelFactory()
            .create(xmlRepositoryConfiguration, databaseMetadata);

        final String output = getOutput();
        final String recordsOutput = getRecordsOutput(repositoryCodeModel);

        // generate records code ---------------------------------------------------------------------------------------
        for (final RecordCodeModel record : repositoryCodeModel.records()) {
            templateProcessor.process(
                Map.of(
                    "record", record
                ),
                "record.ftlh",
                recordsOutput + "/" + record.simpleName() + ".java"
            );
        }
    }

    private @Nonnull DatabaseMetadata createDatabaseMetadata() {
        try (final Connection connection = DriverManager.getConnection(
            xmlRepositoryConfiguration.getConnection().getUrl(),
            xmlRepositoryConfiguration.getConnection().getUsername(),
            xmlRepositoryConfiguration.getConnection().getPassword()
        )) {
            return new DatabaseMetadataFactory().create(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private @Nonnull String getOutput() {
        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput();
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath().toString();
        }
    }

    private @Nonnull String getRecordsOutput(
        final @Nonnull RepositoryCodeModel repositoryCodeModel
    ) {
        final String recordsPackagePath = repositoryCodeModel.recordsPackageName()
            .replace(".", "/");

        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput() + "/" + recordsPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath().toString() + "/" + recordsPackagePath;
        }
    }
}
