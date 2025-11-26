package dev.datlin.commands;

import dev.datlin.Services;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import picocli.CommandLine.Option;

public abstract class BaseCommand implements Runnable {
    @Option(names = {"-v", "--verbose"})
    private @Nonnull Integer verbose = 0;

    @Option(names = {"-wd", "--working-directory"})
    private @Nullable String workingDirectory;

    @Option(names = {"-c", "--config"}, defaultValue = "datlin-config.xml")
    private @Nonnull String config = "datlin-config.xml";

    protected Services services;

    abstract void handleCommand();

    @Override
    public void run() {
        // services = new Services(
        //     workingDirectory != null ? workingDirectory : System.getProperty("user.dir")
        // );

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