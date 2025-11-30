package io.datlin.rcm;

import io.datlin.sql.metadata.DatabaseMetadata;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryCodeModelFactory {
    public RepositoryCodeModel create(
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration,
        final @Nonnull DatabaseMetadata databaseMetadata
    ) {
        final @Nonnull String repositoryPackageName = xmlRepositoryConfiguration.getPackage();
        final @Nonnull String recordsPackageName = repositoryPackageName + ".records";
        final @Nonnull List<RecordCodeModel> records = new ArrayList<>();

        for (final DatabaseMetadata.Table table : databaseMetadata.tables()) {
            records.add(createRecordCodeModel(
                recordsPackageName,
                table,
                xmlRepositoryConfiguration)
            );
        }

        return new RepositoryCodeModel(
            repositoryPackageName,
            recordsPackageName,
            records
        );
    }

    private @Nonnull RecordCodeModel createRecordCodeModel(
        final @Nonnull String recordsPackageName,
        final @Nonnull DatabaseMetadata.Table table,
        final @Nonnull XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        final ArrayList<RecordFieldCodeModel<?>> fields = table.columns().stream()
            .map(this::createRecordFieldCodeModel)
            .collect(Collectors.toCollection(ArrayList::new));

        final String simpleName = toPascalCase(table.name());
        final String canonicalName = recordsPackageName + "." + simpleName;

        return new RecordCodeModel(
            table.name(),
            simpleName,
            canonicalName,
            recordsPackageName,
            fields
        );
    }

    /**
     * Converts a string (e.g., snake_case, kebab-case, camelCase)
     * into PascalCase (e.g., "my_data" -> "MyData").
     * * @param name The input string to convert.
     *
     * @return The converted string in PascalCase format.
     */
    private @Nonnull String toPascalCase(final @Nonnull String name) {
        // Step 1: Normalize separators. Replace hyphens and spaces with underscores.
        final String underscoredName = name.replace('-', '_').replace(' ', '_');

        // Step 2: Handle cases where the string might already be Pascal/CamelCase
        // (i.e., it contains no underscores).
        if (!underscoredName.contains("_")) {
            // If there are no separators, ensure the first character is capitalized.
            // E.g., "mydata" -> "Mydata", "camelCase" -> "CamelCase"
            return Character.toUpperCase(underscoredName.charAt(0)) + underscoredName.substring(1);
        }

        // Step 3: Process the separated string (e.g., snake_case).
        final String[] parts = underscoredName.split("_");
        final StringBuilder pascalCaseString = new StringBuilder();

        for (final String part : parts) {
            // Skip empty parts that might result from multiple consecutive underscores
            if (part.isEmpty()) {
                continue;
            }

            // Step 4: For every part, capitalize the first letter and append the rest in lowercase.
            // This ensures every word starts with a capital letter (PascalCase).
            pascalCaseString.append(Character.toUpperCase(part.charAt(0)));
            pascalCaseString.append(part.substring(1).toLowerCase());
        }

        return pascalCaseString.toString();
    }

    private @Nonnull String toCamelCase(final @Nonnull String name) {
        if (name.isEmpty()) {
            return name;
        }

        // Replace common separators (hyphens and spaces) with underscores
        final String underscoredName = name.replace('-', '_').replace(' ', '_');

        // Handle existing CamelCase:
        // If the string already looks like camelCase (i.e., contains no underscores),
        // we return it as is, or handle the initial capitalization if needed.
        if (!underscoredName.contains("_")) {
            // If the first character is uppercase, it's PascalCase. Convert to camelCase.
            if (Character.isUpperCase(underscoredName.charAt(0))) {
                return Character.toLowerCase(underscoredName.charAt(0)) + underscoredName.substring(1);
            }
            // Otherwise, assume it's already camelCase (or a single word) and return it.
            return underscoredName;
        }

        // Process snake_case or similar strings
        final String[] parts = underscoredName.split("_");
        final StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            final String part = parts[i];

            // Skip empty parts that could result from multiple consecutive underscores
            if (part.isEmpty()) {
                continue;
            }

            if (i == 0) {
                // For the first word, convert to lowercase (standard camelCase)
                camelCaseString.append(part.toLowerCase());
            } else {
                // For subsequent words, capitalize the first letter and lowercase the rest
                camelCaseString.append(Character.toUpperCase(part.charAt(0)));
                camelCaseString.append(part.substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }

    private <T> Class<T> getJavaType(final @Nonnull String type) {
        final String normalizedType = type.toLowerCase().split("\\(")[0].trim();
        final Class<?> javaClass = switch (normalizedType) {
            // Numeric Types
            case "smallint", "int2" -> Short.class;
            case "integer", "int4" -> Integer.class;
            case "bigint", "int8", "serial", "bigserial" -> Long.class;
            case "real", "float4" -> Float.class;
            case "double precision", "float8" -> Double.class;
            case "numeric", "decimal" -> java.math.BigDecimal.class;

            // Character/String Types
            case "varchar", "character varying", "char", "character", "text" -> String.class;
            case "uuid" -> java.util.UUID.class;

            // Boolean Type
            case "boolean", "bool" -> Boolean.class;

            // Date/Time Types (Using Java 8+ time classes)
            case "date" -> java.time.LocalDate.class;
            case "time", "time without time zone" -> java.time.LocalTime.class;
            case "timestamp", "timestamp without time zone" -> java.time.LocalDateTime.class;
            case "timestamptz", "timestamp with time zone" -> java.time.OffsetDateTime.class;
            case "timetz", "time with time zone" -> java.time.OffsetTime.class;

            // Binary/LOB Types
            case "bytea" -> byte[].class; // Often mapped to byte array

            // Special/Other Types
            case "json", "jsonb" -> String.class; // Often treated as a String, or a custom type/library object

            // Default Case for Unsupported or Unknown Types
            default -> throw new IllegalArgumentException("Unsupported PostgreSQL type: " + type);
        };

        // This cast is necessary to satisfy the method's generic return type `Class<T>`
        //noinspection unchecked
        return (Class<T>) javaClass;
    }

    private @Nonnull RecordFieldCodeModel<?> createRecordFieldCodeModel(
        final @Nonnull DatabaseMetadata.Column column
    ) {
        final @Nonnull String name = toCamelCase(column.name());
        final Class<?> javaType = getJavaType(column.type());
        return new RecordFieldCodeModel<>(name, javaType, column.nullable());
    }
}