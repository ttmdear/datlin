package dev.datlin;

import dev.datlin.gen.GenerateService;
import dev.datlin.util.FilesUtil;
import dev.datlin.util.PathUtil;
import dev.datlin.xrc.RepositoryConfiguration;
import dev.datlin.xrc.RepositoryConfigurationFactory;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.file.Path;

public class Services {
    private final @Nonnull Integer verbose;
    private final @Nonnull String workingDirectoryPath;
    private final @Nonnull String repositoryConfigurationPath;

    private @Nullable PathUtil pathUtil;
    private @Nullable FilesUtil filesUtil = null;
    private @Nullable GenerateService generateService = null;
    private @Nullable RepositoryConfiguration repositoryConfiguration = null;

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

    public synchronized @Nonnull GenerateService generateService() {
        if (generateService == null) {
            generateService = new GenerateService();
        }

        return generateService;
    }

    public synchronized @Nonnull RepositoryConfiguration repositoryConfiguration() {
        if (repositoryConfiguration == null) {
            final RepositoryConfigurationFactory factory = new RepositoryConfigurationFactory(filesUtil());

            repositoryConfiguration = factory.create(repositoryConfigurationPath);
        }

        return repositoryConfiguration;
    }
}
