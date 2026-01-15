package io.datlin.gec;

import io.datlin.frm.TemplateProcessor;
import io.datlin.rcm.RecordCodeModel;
import io.datlin.rcm.RepositoryCodeModel;
import io.datlin.rcm.RepositoryCodeModelFactory;
import io.datlin.rcm.RepositoryCodeModelV1;
import io.datlin.rcm.TableCodeModel;
import io.datlin.rcm.TableExecutionCodeModel;
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
    private final XmlRepositoryConfiguration xrc;

    @Nonnull
    private final TemplateProcessor templateProcessor;

    public RepositoryCodeGenerator(
        @Nonnull final String xmlRepositoryConfigurationPath,
        @Nonnull final XmlRepositoryConfiguration xrc,
        @Nonnull final TemplateProcessor templateProcessor
    ) {
        this.xmlRepositoryConfigurationPath = xmlRepositoryConfigurationPath;
        this.xrc = xrc;
        this.templateProcessor = templateProcessor;
    }

    public void generate() {
        final DatabaseMetadata databaseMetadata = createDatabaseMetadata();
        final RepositoryCodeModel repository = new RepositoryCodeModelFactory(xrc,
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
        for (final TableCodeModel table : repository.getDatabase().getTables()) {
            templateProcessor.process(
                Map.of(
                    "table", table
                ),
                "table.ftlh",
                getJavaOutput(table.getCanonicalName())
            );

            final RecordCodeModel record = table.getRecord();

            templateProcessor.process(
                Map.of(
                    "record", record
                ),
                "record.ftlh",
                getJavaOutput(record.getCanonicalName())
            );

            final TableExecutionCodeModel execution = table.getExecution();

            templateProcessor.process(
                Map.of(
                    "execution", execution
                ),
                "execution.ftlh",
                getJavaOutput(execution.getCanonicalName())
            );
        }
    }

    private @Nonnull DatabaseMetadata createDatabaseMetadata() {
        try (final Connection connection = DriverManager.getConnection(
            xrc.getConnection().getUrl(),
            xrc.getConnection().getUsername(),
            xrc.getConnection().getPassword()
        )) {
            return new DatabaseMetadataFactory().create(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private @Nonnull String getOutput() {
        if (Path.of(xrc.getOutput()).isAbsolute()) {
            return xrc.getOutput();
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xrc.getOutput())
                .toAbsolutePath().toString();
        }
    }

    @Nonnull
    private String getDatabaseOutput(
        @Nonnull final RepositoryCodeModel repository
    ) {
        final String databasePath = repository.getPackageName()
            .replace(".", "/");

        if (Path.of(xrc.getOutput()).isAbsolute()) {
            return xrc.getOutput() + "/" + databasePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xrc.getOutput())
                .toAbsolutePath() + "/" + databasePath;
        }
    }

    @Nonnull
    private String getJavaOutput(
        @Nonnull final String canonicalName

    ) {
        final Path outputXrc = Path.of(xrc.getOutput());
        final Path path = Path.of(canonicalName.replaceAll("\\.", "/"));

        if (outputXrc.isAbsolute()) {
            return outputXrc.resolve(path)
                .toString();
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xrc.getOutput())
                .resolve(path)
                .toAbsolutePath() + ".java";
        }
    }

    @Nonnull
    private String getRecordsOutput(
        @Nonnull final RepositoryCodeModelV1 repositoryCodeModel
    ) {
        final String recordsPackagePath = repositoryCodeModel.getRecordsPackageName()
            .replace(".", "/");

        if (Path.of(xrc.getOutput()).isAbsolute()) {
            return xrc.getOutput() + "/" + recordsPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xrc.getOutput())
                .toAbsolutePath() + "/" + recordsPackagePath;
        }
    }

    @Nonnull
    private String getExecutionsOutput(
        @Nonnull final RepositoryCodeModelV1 repositoryCodeModel
    ) {
        final String executionsPackagePath = repositoryCodeModel.getExecutionsPackageName()
            .replace(".", "/");

        if (Path.of(xrc.getOutput()).isAbsolute()) {
            return xrc.getOutput() + "/" + executionsPackagePath;
        } else {
            return Path.of(xmlRepositoryConfigurationPath).getParent()
                .resolve(xrc.getOutput())
                .toAbsolutePath() + "/" + executionsPackagePath;
        }
    }
}
