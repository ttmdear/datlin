package dev.datlin.cmd;

import dev.datlin.rcm.RepositoryCodeModel;
import dev.datlin.xrc.XmlRepositoryConfiguration;
import dev.datlin.xrc.generated.XmlRepositoryConfiguration;
import picocli.CommandLine.Command;

@Command(
    name = "generate",
    description = "Generate classes to connect with database."
)
final class GenerateCommand extends BaseCommand {
    @Override
    void handleCommand() {
        final RepositoryCodeModel repositoryCodeModel = services.repositoryCodeModel();

        System.out.printf("test");
        // try {
        //     // final JAXBContext context = JAXBContext.newInstance("dev.datlin.configuration");
        //     // final Unmarshaller unmarshaller = context.createUnmarshaller();
        //     // final Object obj = unmarshaller.unmarshal(new File("/home/inspipi/desktop/traisit/traisit-core/src/main/resources/traisit-db-datlin.xml"));

        //     // datlin-repository.plsPlans().find()
        //     // traisit.get
        //     //
        //     // System.out.println("test");
        // } catch (JAXBException e) {
        //     throw new RuntimeException(e);
        // }
        // System.out.printf("test");
    }
}
