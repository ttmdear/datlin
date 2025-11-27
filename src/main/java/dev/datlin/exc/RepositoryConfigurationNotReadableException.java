package dev.datlin.exc;

import jakarta.annotation.Nonnull;

public class RepositoryConfigurationNotReadableException extends RuntimeException {
    public RepositoryConfigurationNotReadableException(final @Nonnull String repositoryConfigurationPath) {
        super("Repository configuration file " + repositoryConfigurationPath + " is not readable");
    }
}
