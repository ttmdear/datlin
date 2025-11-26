package dev.datlin;

import java.nio.file.Path;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class Services
{
    private final @Nonnull String workingDirectory;

    private @Nullable PathUtil pathUtil = null;

    private @Nullable FilesUtil filesUtil = null;

    public Services(final @Nonnull String workingDirectory)
    {
        this.pathUtil = new PathUtil();
        this.workingDirectory = pathUtil.expand(Path.of(workingDirectory)).toString();
    }

    public @Nonnull String workingDirectory()
    {
        return this.workingDirectory;
    }

    public synchronized @Nonnull PathUtil pathUtil()
    {
        if (pathUtil == null)
        {
            pathUtil = new PathUtil();
        }

        return pathUtil;
    }

    public synchronized @Nonnull FilesUtil filesUtil()
    {
        if (filesUtil == null)
        {
            filesUtil = new FilesUtil();
        }

        return filesUtil;
    }
}
