package dev.datlin.commands;

import picocli.CommandLine.Command;

@Command(
    name = "generate",
    description = "Generate classes to connect with database."
)
final class GenerateCommand extends BaseCommand {
    @Override
    void handleCommand() {
        System.out.printf("test");
    }
}
