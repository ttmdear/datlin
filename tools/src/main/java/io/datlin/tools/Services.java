package io.datlin.tools;

import io.datlin.gec.RepositoryCodeGenerator;
import io.datlin.rcm.RepositoryCodeModel;
import io.datlin.rpm.RepositoryModel;
import io.datlin.sql.metadata.DatabaseMetadataFactory;
import io.datlin.util.FilesUtil;
import io.datlin.util.PathUtil;
import io.datlin.xrc.XmlRepositoryConfigurationFactory;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.file.Path;

public class Services {
    private final @Nonnull Integer verbose;
    private final @Nonnull String workingDirectoryPath;
    private final @Nonnull String repositoryConfigurationPath;

    private @Nullable PathUtil pathUtil;
    private @Nullable FilesUtil filesUtil = null;

    private @Nullable XmlRepositoryConfiguration xmlRepositoryConfiguration = null;
    private @Nullable DatabaseMetadataFactory databaseMetadataFactory = null;
    private @Nullable RepositoryCodeGenerator repositoryCodeGenerator = null;
    private @Nullable RepositoryCodeModel repositoryCodeModel = null;
    private @Nullable RepositoryModel repositoryModel = null;

    public Services(
        final @Nonnull Integer verbose,
        final @Nonnull String workingDirectoryPath,
        final @Nonnull String repositoryConfigurationPath
    ) {
        this.pathUtil = new PathUtil();
        this.verbose = verbose;
        this.workingDirectoryPath = pathUtil.expand(Path.of(workingDirectoryPath)).toString();

        // preparing repository configuration path ---------------------------------------------------------------------
        Path workingDirectoryPath1 = pathUtil.expand(Path.of(repositoryConfigurationPath));

        if (!workingDirectoryPath1.isAbsolute()) {
            workingDirectoryPath1 = workingDirectoryPath1.resolve(workingDirectoryPath1);
        }

        this.repositoryConfigurationPath = workingDirectoryPath1.toString();
    }

    public @Nonnull Integer verbose() {
        return verbose;
    }

    public @Nonnull String workingDirectoryPath() {
        return this.workingDirectoryPath;
    }

    public @Nonnull String repositoryConfigurationPath() {
        return this.repositoryConfigurationPath;
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
            repositoryCodeGenerator = new RepositoryCodeGenerator(xmlRepositoryConfiguration());
        }

        return repositoryCodeGenerator;
    }

    public synchronized @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration() {
        if (xmlRepositoryConfiguration == null) {
            xmlRepositoryConfiguration = new XmlRepositoryConfigurationFactory(filesUtil())
                .create(repositoryConfigurationPath);
        }

        return xmlRepositoryConfiguration;
    }

    public synchronized @Nonnull RepositoryCodeModel repositoryCodeModel() {
        // if (repositoryCodeModel == null) {
        //     // repositoryCodeModel = new RepositoryCodeModelFactory().c
        // }

        return repositoryCodeModel;
    }

    public synchronized @Nonnull RepositoryModel repositoryModel() {
        // if (repositoryModel == null) {
        //     repositoryModel = new RepositoryModelFactory().create(repositoryConfigurationPath);
        // }

        return null;
    }
}
