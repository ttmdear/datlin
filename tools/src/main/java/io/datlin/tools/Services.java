package io.datlin.tools;

import io.datlin.frm.TemplateProcessor;
import io.datlin.gec.RepositoryCodeGenerator;
import io.datlin.sql.mtd.DatabaseMetadataFactory;
import io.datlin.util.FilesUtil;
import io.datlin.util.PathUtil;
import io.datlin.xrc.XmlRepositoryConfigurationFactory;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.file.Path;

public class Services {
    private @Nonnull final Integer verbose;
    private @Nonnull final String workingDirectoryPath;
    private @Nonnull final String xmlRepositoryConfigurationPath;

    private @Nullable PathUtil pathUtil;
    private @Nullable FilesUtil filesUtil = null;

    private @Nullable XmlRepositoryConfiguration xmlRepositoryConfiguration = null;
    private @Nullable DatabaseMetadataFactory databaseMetadataFactory = null;
    private @Nullable RepositoryCodeGenerator repositoryCodeGenerator = null;
    private @Nullable TemplateProcessor templateProcessor = null;

    public Services(
        @Nonnull final Integer verbose,
        @Nonnull final String workingDirectoryPath,
        @Nonnull final String xmlRepositoryConfigurationPath
    ) {
        this.pathUtil = new PathUtil();
        this.verbose = verbose;
        this.workingDirectoryPath = pathUtil.expand(Path.of(workingDirectoryPath)).toString();

        // preparing repository configuration path ---------------------------------------------------------------------
        Path xmlRepositoryConfigurationPath1 = pathUtil.expand(Path.of(xmlRepositoryConfigurationPath));

        if (!xmlRepositoryConfigurationPath1.isAbsolute()) {
            xmlRepositoryConfigurationPath1 = Path.of(workingDirectoryPath).resolve(xmlRepositoryConfigurationPath1);
        }

        this.xmlRepositoryConfigurationPath = xmlRepositoryConfigurationPath1.toString();
    }

    public @Nonnull Integer verbose() {
        return verbose;
    }

    public @Nonnull String workingDirectoryPath() {
        return this.workingDirectoryPath;
    }

    public @Nonnull String repositoryConfigurationPath() {
        return this.xmlRepositoryConfigurationPath;
    }

    public synchronized @Nonnull PathUtil pathUtil() {
        if (pathUtil == null) {
            pathUtil = new PathUtil();
        }

        return pathUtil;
    }

    public synchronized @Nonnull FilesUtil filesUtil() {
        if (filesUtil == null) {
            filesUtil = new FilesUtil();
        }

        return filesUtil;
    }

    public synchronized @Nonnull RepositoryCodeGenerator repositoryCodeGenerator() {
        if (repositoryCodeGenerator == null) {
            repositoryCodeGenerator = new RepositoryCodeGenerator(
                xmlRepositoryConfigurationPath,
                xmlRepositoryConfiguration(),
                templateProcessor()
            );
        }

        return repositoryCodeGenerator;
    }

    public synchronized @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration() {
        if (xmlRepositoryConfiguration == null) {
            xmlRepositoryConfiguration = new XmlRepositoryConfigurationFactory(filesUtil())
                .create(xmlRepositoryConfigurationPath);
        }

        return xmlRepositoryConfiguration;
    }

    public synchronized @Nonnull TemplateProcessor templateProcessor() {
        if (templateProcessor == null) {
            templateProcessor = new TemplateProcessor(filesUtil());
        }

        return templateProcessor;
    }
}
