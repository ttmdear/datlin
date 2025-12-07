package io.datlin.xrc.exc;

import jakarta.annotation.Nonnull;

public class RepositoryConfigurationNotReadableException extends RuntimeException {
    public RepositoryConfigurationNotReadableException(@Nonnull final String repositoryConfigurationPath) {
        super("Repository configuration file " + repositoryConfigurationPath + " is not readable");
    }
}
