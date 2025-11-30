package io.datlin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import jakarta.annotation.Nonnull;

public class FilesUtil {
    public @Nonnull InputStream getInputStreamFromResource(final @Nonnull String path) {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new NoSuchElementException(path);
        }

        return inputStream;
    }

    public @Nonnull InputStream getInputStream(final @Nonnull Path path) {
        try {
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("File does not exist: " + path);
            }

            if (!Files.isReadable(path)) {
                throw new IllegalArgumentException("File is not readable: " + path);
            }

            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to open InputStream for path: " + path, e);
        }
    }

    public long getSize(final @Nonnull Path path) {
        try {
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("File does not exist: " + path);
            }

            if (!Files.isReadable(path)) {
                throw new IllegalArgumentException("File is not readable: " + path);
            }

            return Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open InputStream for path: " + path, e);
        }
    }

    public boolean exists(
        final @Nonnull Path path,
        final @Nonnull LinkOption... options
    ) {
        return Files.exists(path, options);
    }

    public boolean isReadable(final @Nonnull Path path) {
        return Files.isReadable(path);
    }

    public void copy(
        final @Nonnull InputStream inputStream,
        final @Nonnull Path target,
        final @Nonnull CopyOption... options
    ) {
        try {
            Files.createDirectories(target.getParent());
            Files.copy(inputStream, target, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(final @Nonnull Path path) {
        try {
            if (Files.notExists(path)) {
                throw new IllegalArgumentException("File does not exist: " + path);
            }
            if (Files.isDirectory(path)) {
                try (Stream<Path> entries = Files.list(path)) {
                    entries.forEach(this::delete);
                }
            }
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete: " + path, e);
        }
    }

    public @Nonnull Stream<Path> walk(
        final @Nonnull Path start,
        final @Nonnull FileVisitOption... options
    ) {
        try {
            return Files.walk(start, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nonnull Stream<Path> walk(
        final @Nonnull Path start,
        final @Nonnull List<String> exclude,
        final @Nonnull List<String> include,
        final @Nonnull FileVisitOption... options
    ) {
        final List<PathMatcher> includeMatchers = include.stream()
            .map(pattern -> FileSystems.getDefault().getPathMatcher("glob:" + pattern))
            .toList();

        final List<PathMatcher> excludeMatchers = exclude.stream()
            .map(pattern -> FileSystems.getDefault().getPathMatcher("glob:" + pattern))
            .toList();

        try {
            return Files.walk(start, options)
                .filter(Files::isRegularFile)
                .filter(p -> {
                    // include check
                    boolean included = includeMatchers.isEmpty()
                        || includeMatchers.stream().anyMatch(m -> m.matches(p));
                    if (!included) return false;

                    // exclude check
                    return excludeMatchers.stream().noneMatch(m -> m.matches(p));
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to walk path: " + start, e);
        }
    }

    public @Nonnull Stream<Path> list(final @Nonnull Path dir) {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isDirectory(final @Nonnull Path path, final @Nonnull LinkOption... options) {
        return Files.isDirectory(path, options);
    }

    public void createDirectories(final @Nonnull Path dir, final @Nonnull FileAttribute<?>... attrs) {
        try {
            Files.createDirectories(dir, attrs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty(final @Nonnull Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> entries = Files.list(path)) {
                    return entries.findFirst().isEmpty();
                }
            } else {
                return Files.size(path) == 0;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to check if path is empty: " + path, e);
        }
    }

    public @Nonnull Path getFileName(final @Nonnull Path path) {
        return getFileName(path, false);
    }

    public @Nonnull Path getFileName(final @Nonnull Path path, boolean noExtension) {
        final Path fileName = path.getFileName();

        if (fileName == null) {
            throw new IllegalArgumentException("Ścieżka nie zawiera nazwy pliku: " + path);
        }

        final String name = fileName.toString();
        if (noExtension) {
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                return Path.of(name.substring(0, dotIndex));
            }
        }
        return Path.of(name);
    }

    public @Nonnull Long getLastModifiedTime(final @Nonnull Path file) {
        try {
            return Files.getLastModifiedTime(file).toMillis();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read last modified time for: " + file, e);
        }
    }

    public void setLastModifiedTime(final @Nonnull Path path, final long time) {
        try {
            Files.setLastModifiedTime(path, FileTime.fromMillis(time));
        } catch (IOException e) {
            throw new RuntimeException("Cannot set last modified time for: " + path, e);
        }
    }

    public long calculateCRC32(final @Nonnull Path path) throws IOException {
        final File file = path.toFile();
        try (final InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            final CRC32 crc = new CRC32();
            try (final CheckedInputStream checkedInputStream = new CheckedInputStream(in, crc)) {
                byte[] buffer = new byte[4096];
                //noinspection StatementWithEmptyBody
                while (checkedInputStream.read(buffer) >= 0) {
                    // czytanie wystarczy, suma się liczy automatycznie
                }
                return crc.getValue();
            }
        }
    }
}