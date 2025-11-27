package dev.datlin;

import dev.datlin.generate.GenerateService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.nio.file.Path;

public class Services {
    private final @Nonnull String workingDirectory;
    private @Nullable PathUtil pathUtil = null;
    private @Nullable FilesUtil filesUtil = null;
    private @Nullable GenerateService generateService = null;

    public Services(final @Nonnull String workingDirectory) {
        this.pathUtil = new PathUtil();
        this.workingDirectory = pathUtil.expand(Path.of(workingDirectory)).toString();
    }

    public @Nonnull String workingDirectory() {
        return this.workingDirectory;
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
}
