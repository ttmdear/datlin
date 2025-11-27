package dev.datlin.xrc;

import dev.datlin.util.FilesUtil;
import dev.datlin.exc.RepositoryConfigurationNotReadableException;
import jakarta.annotation.Nonnull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Path;

public class RepositoryConfigurationFactory {
    private final @Nonnull FilesUtil filesUtil;

    public RepositoryConfigurationFactory(final @Nonnull FilesUtil filesUtil) {
        this.filesUtil = filesUtil;
    }

    public RepositoryConfiguration create(
        final @Nonnull String repositoryConfigurationPath
    ) {
        final Path repositoryConfigurationPath1 = Path.of(repositoryConfigurationPath);

        if (!filesUtil.exists(repositoryConfigurationPath1) ||
            !filesUtil.isReadable(repositoryConfigurationPath1)
        ) {
            throw new RepositoryConfigurationNotReadableException(repositoryConfigurationPath);
        }

        try {
            final JAXBContext context = JAXBContext.newInstance("dev.datlin.repository.configuration.generated");
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Object obj = unmarshaller.unmarshal(new File(repositoryConfigurationPath));

            // databaserepository
            // final dev.datlin.repository.configuration.generated.RepositoryConfiguration

            // datlin - repository.plsPlans().find()
            // traisit.get

            System.out.println("test");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
