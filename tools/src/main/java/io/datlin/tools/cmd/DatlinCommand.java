package io.datlin.tools.cmd;

import jakarta.annotation.Nonnull;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "datlin",
    mixinStandardHelpOptions = true,
    version = "datlin 1.0"
)
public class DatlinCommand extends BaseCommand {

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new DatlinCommand());
        cmd.addSubcommand("generate", new GenerateCommand());
        cmd.execute(args);
    }

    @Override
    void handleCommand() {
        throw new RuntimeException("Nie podano podkomendy. Użyj --help, aby zobaczyć dostępne podkomendy.");
    }
}
