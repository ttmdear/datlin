package io.datlin.gec;

import io.datlin.frm.TemplateProcessor;
import io.datlin.rcm.ExecutionCodeModel;
import io.datlin.rcm.RecordCodeModel;
import io.datlin.rcm.RepositoryCodeModelV1;
import io.datlin.rcm.RepositoryCodeModelFactory;
import io.datlin.rcm.TableCodeModelV1;
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

    @Nonnull
    private final String xmlRepositoryConfigurationPath;

    @Nonnull
    private final XmlRepositoryConfiguration xmlRepositoryConfiguration;

    @Nonnull
    private final TemplateProcessor templateProcessor;

    public RepositoryCodeGenerator(
        @Nonnull final String xmlRepositoryConfigurationPath,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration,
        @Nonnull final TemplateProcessor templateProcessor
    ) {
        this.xmlRepositoryConfigurationPath = xmlRepositoryConfigurationPath;
        this.xmlRepositoryConfiguration = xmlRepositoryConfiguration;
        this.templateProcessor = templateProcessor;
    }

    public void generate() {
        final DatabaseMetadata databaseMetadata = createDatabaseMetadata();
        final RepositoryCodeModelV1 repository = new RepositoryCodeModelFactory(xmlRepositoryConfiguration,
            databaseMetadata).create();

        final String output = getOutput();

        // generate database code --------------------------------------------------------------------------------------
        final String databaseOutput = getDatabaseOutput(repository);
        templateProcessor.process(
            Map.of(
                "database", repository.getDatabase()
            ),
            "database.ftlh",
            databaseOutput + "/" + repository.getDatabase().getSimpleName() + ".java"
        );

        // generate tables code ----------------------------------------------------------------------------------------
        final String tablesOutput = getTablesOutput(repository);
        for (final TableCodeModelV1 table : repository.getTables()) {
            templateProcessor.process(
                Map.of(
                    "table", table
                ),
                "table.ftlh",
                tablesOutput + "/" + table.getSimpleName() + ".java"
            );
        }

        // generate records code ---------------------------------------------------------------------------------------
        final String recordsOutput = getRecordsOutput(repository);
        for (final RecordCodeModel record : repository.getRecords()) {
            templateProcessor.process(
                Map.of(
                    "record", record
                ),
                "record.ftlh",
                recordsOutput + "/" + record.getSimpleName() + ".java"
            );
        }

        // generate executions code ------------------------------------------------------------------------------------
        final String executionsOutput = getExecutionsOutput(repository);

        for (final ExecutionCodeModel execution : repository.getExecutions()) {
            templateProcessor.process(
                Map.of(
                    "execution", execution
                ),
                "execution.ftlh",
                executionsOutput + "/" + execution.getSimpleName() + ".java"
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

    @Nonnull
    private String getDatabaseOutput(
        @Nonnull final RepositoryCodeModelV1 repository
    ) {
        final String databasePath = repository.getPackageName()
            .replace(".", "/");

        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput() + "/" + databasePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath() + "/" + databasePath;
        }
    }

    @Nonnull
    private String getTablesOutput(
        @Nonnull final RepositoryCodeModelV1 repositoryCodeModel
    ) {
        final String tablesPackagePath = repositoryCodeModel.getTablesPackageName()
            .replace(".", "/");

        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput() + "/" + tablesPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath() + "/" + tablesPackagePath;
        }
    }

    @Nonnull
    private String getRecordsOutput(
        @Nonnull final RepositoryCodeModelV1 repositoryCodeModel
    ) {
        final String recordsPackagePath = repositoryCodeModel.getRecordsPackageName()
            .replace(".", "/");

        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput() + "/" + recordsPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath() + "/" + recordsPackagePath;
        }
    }

    @Nonnull
    private String getExecutionsOutput(
        @Nonnull final RepositoryCodeModelV1 repositoryCodeModel
    ) {
        final String executionsPackagePath = repositoryCodeModel.getExecutionsPackageName()
            .replace(".", "/");

        if (Path.of(xmlRepositoryConfiguration.getOutput()).isAbsolute()) {
            return xmlRepositoryConfiguration.getOutput() + "/" + executionsPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xmlRepositoryConfiguration.getOutput())
                .toAbsolutePath() + "/" + executionsPackagePath;
        }
    }
}
