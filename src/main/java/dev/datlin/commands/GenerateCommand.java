package dev.datlin.commands;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import picocli.CommandLine.Command;

import java.io.File;

@Command(
    name = "generate",
    description = "Generate classes to connect with database."
)
final class GenerateCommand extends BaseCommand {
    @Override
    void handleCommand() {
        try {
            // final JAXBContext context = JAXBContext.newInstance("dev.datlin.configuration");
            // final Unmarshaller unmarshaller = context.createUnmarshaller();
            // final Object obj = unmarshaller.unmarshal(new File("/home/inspipi/desktop/traisit/traisit-core/src/main/resources/traisit-db-datlin.xml"));

            // datlin-repository.plsPlans().find()
            // traisit.get
            //
            // System.out.println("test");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        // System.out.printf("test");
    }
}
