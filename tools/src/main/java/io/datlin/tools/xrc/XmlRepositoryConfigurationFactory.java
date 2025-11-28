package io.datlin.tools.xrc;

import io.datlin.tools.exc.RepositoryConfigurationNotReadableException;
import io.datlin.tools.util.FilesUtil;
import io.datlin.tools.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Path;

public class XmlRepositoryConfigurationFactory {
    private final @Nonnull FilesUtil filesUtil;

    public XmlRepositoryConfigurationFactory(final @Nonnull FilesUtil filesUtil) {
        this.filesUtil = filesUtil;
    }

    public XmlRepositoryConfiguration create(
        final @Nonnull String repositoryConfigurationPath
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
        final @Nonnull String repositoryConfigurationPath
    ) {
        try {
            final JAXBContext context = JAXBContext.newInstance("io.datlin.tools.xrc.generated");
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
