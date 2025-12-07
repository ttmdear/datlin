package io.datlin.xrc;

import io.datlin.xrc.exc.RepositoryConfigurationNotReadableException;
import io.datlin.util.FilesUtil;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Path;

public class XmlRepositoryConfigurationFactory {
    private @Nonnull final FilesUtil filesUtil;

    public XmlRepositoryConfigurationFactory(@Nonnull final FilesUtil filesUtil) {
        this.filesUtil = filesUtil;
    }

    public XmlRepositoryConfiguration create(
        @Nonnull final String repositoryConfigurationPath
    ) {
        final Path repositoryConfigurationPath1 = Path.of(repositoryConfigurationPath);

        if (!filesUtil.exists(repositoryConfigurationPath1) ||
            !filesUtil.isReadable(repositoryConfigurationPath1)
        ) {
            throw new RepositoryConfigurationNotReadableException(repositoryConfigurationPath);
        }

        return loadXmlRepositoryConfiguration(repositoryConfigurationPath);
    }

    private @Nonnull XmlRepositoryConfiguration loadXmlRepositoryConfiguration(
        @Nonnull final String repositoryConfigurationPath
    ) {
        try {
            final JAXBContext context = JAXBContext.newInstance("io.datlin.xrc.generated");
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Object object = unmarshaller.unmarshal(new File(repositoryConfigurationPath));

            if (!(object instanceof XmlRepositoryConfiguration)) {
                throw new RepositoryConfigurationNotReadableException(repositoryConfigurationPath);
            }

            return (XmlRepositoryConfiguration) object;
        } catch (JAXBException e) {
            throw new RepositoryConfigurationNotReadableException(repositoryConfigurationPath);
        }
    }
}
