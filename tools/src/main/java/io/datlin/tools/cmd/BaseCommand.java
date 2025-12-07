package io.datlin.tools.cmd;

import jakarta.annotation.Nullable;
import io.datlin.tools.Services;
import picocli.CommandLine.Option;

public abstract class BaseCommand implements Runnable {
    @Option(names = {"-v", "--verbose"}, defaultValue = "0")
    @Nullable
    private Integer verbose;

    @Option(names = {"-wd", "--working-directory"})
    @Nullable
    private String workingDirectory;

    @Option(names = {"-rc", "--repository-configuration"}, defaultValue = "datlin-repository-configuration.xml")
    @Nullable
    private String repositoryConfiguration;

    @Nullable
    protected Services services;

    abstract void handleCommand();

    @Override
    public void run() {
        services = new Services(
            verbose == null ? 0 : verbose,
            workingDirectory != null ? workingDirectory : System.getProperty("user.dir"),
            repositoryConfiguration != null ? repositoryConfiguration : "datlin-repository-configuration.xml"
        );

        try {
            handleCommand();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (verbose > 0) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }
}