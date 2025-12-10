package io.datlin.gec;

import io.datlin.frm.TemplateProcessor;
import io.datlin.rcm.ExecutionCodeModel;
import io.datlin.rcm.RecordCodeModel;
import io.datlin.rcm.RepositoryCodeModel;
import io.datlin.rcm.RepositoryCodeModelFactory;
import io.datlin.rcm.TableCodeModel;
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
        final RepositoryCodeModel repositoryCodeModel = new RepositoryCodeModelFactory()
            .create(xmlRepositoryConfiguration, databaseMetadata);

        final String output = getOutput();

        // generate tables code ----------------------------------------------------------------------------------------
        final String tablesOutput = getTablesOutput(repositoryCodeModel);
        for (final TableCodeModel table : repositoryCodeModel.tables()) {
            templateProcessor.process(
                Map.of(
                    "table", table
                ),
                "table.ftlh",
                tablesOutput + "/" + table.simpleName() + ".java"
            );
        }

        // generate records code ---------------------------------------------------------------------------------------
        final String recordsOutput = getRecordsOutput(repositoryCodeModel);
        for (final RecordCodeModel record : repositoryCodeModel.records()) {
            templateProcessor.process(
                Map.of(
                    "record", record
                ),
                "record.ftlh",
                recordsOutput + "/" + record.simpleName() + ".java"
            );
        }

        // generate records code ---------------------------------------------------------------------------------------
        final String executionsOutput = getExecutionsOutput(repositoryCodeModel);

        for (final ExecutionCodeModel execution : repositoryCodeModel.executions()) {
            templateProcessor.process(
                Map.of(
                    "execution", execution
                ),
                "execution.ftlh",
                executionsOutput + "/" + execution.simpleName() + ".java"
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
    private String getTablesOutput(
        @Nonnull final RepositoryCodeModel repositoryCodeModel
    ) {
        final String tablesPackagePath = repositoryCodeModel.tablesPackageName()
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
        @Nonnull final RepositoryCodeModel repositoryCodeModel
    ) {
        final String recordsPackagePath = repositoryCodeModel.recordsPackageName()
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
        @Nonnull final RepositoryCodeModel repositoryCodeModel
    ) {
        final String executionsPackagePath = repositoryCodeModel.executionsPackageName()
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
