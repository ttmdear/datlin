package io.datlin.rcm;

import io.datlin.sql.mtd.DatabaseMetadata;
import io.datlin.xrc.generated.ColumnType;
import io.datlin.xrc.generated.TableType;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class RepositoryCodeModelFactory {
    @Nonnull
    public RepositoryCodeModel create(
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration,
        @Nonnull final DatabaseMetadata databaseMetadata
    ) {
        final String repositoryPackageName = xmlRepositoryConfiguration.getPackage();
        final String recordsPackageName = repositoryPackageName + ".records";
        final String tablesPackageName = repositoryPackageName + ".tables";
        final String executionsPackageName = repositoryPackageName + ".executions";
        final List<TableCodeModel> tables = new ArrayList<>();
        final List<RecordCodeModel> records = new ArrayList<>();
        final List<ExecutionCodeModel> executions = new ArrayList<>();

        for (final DatabaseMetadata.Table table : databaseMetadata.tables()) {
            tables.add(createTableCodeModel(
                tablesPackageName,
                table
            ));

            // create record code model --------------------------------------------------------------------------------
            final RecordCodeModel recordCodeModel = createRecordCodeModel(
                recordsPackageName,
                table,
                xmlRepositoryConfiguration
            );

            records.add(recordCodeModel);

            // create execution code model -----------------------------------------------------------------------------
            final ExecutionCodeModel executionCodeModel = createExecutionCodeModel(
                executionsPackageName,
                table,
                recordCodeModel
            );

            executions.add(executionCodeModel);
        }

        return new RepositoryCodeModel(
            repositoryPackageName,
            recordsPackageName,
            tablesPackageName,
            executionsPackageName,
            unmodifiableList(tables),
            unmodifiableList(records),
            unmodifiableList(executions)
        );
    }

    @Nonnull
    private TableCodeModel createTableCodeModel(
        @Nonnull final String tablesPackageName,
        @Nonnull final DatabaseMetadata.Table table
    ) {
        final String simpleName = toPascalCase(table.name()) + "Table";
        final String canonicalName = tablesPackageName + "." + simpleName;
        final List<TableColumnCodeModel> columns = table.columns().stream()
            .map(column -> new TableColumnCodeModel(
                column.name(),
                column.nullable()
            )).toList();

        return new TableCodeModel(
            table.name(),
            simpleName,
            canonicalName,
            tablesPackageName,
            columns
        );
    }

    @Nonnull
    private RecordCodeModel createRecordCodeModel(
        @Nonnull final String recordsPackageName,
        @Nonnull final DatabaseMetadata.Table table,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        final List<RecordFieldCodeModel> primaryKeys = table.columns().stream()
            .filter(DatabaseMetadata.Column::primaryKey)
            .map(column -> createRecordFieldCodeModel(column, table, xmlRepositoryConfiguration))
            .collect(Collectors.toCollection(ArrayList::new));

        final List<RecordFieldCodeModel> fields = table.columns().stream()
            .map(column -> createRecordFieldCodeModel(column, table, xmlRepositoryConfiguration))
            .collect(Collectors.toCollection(ArrayList::new));

        final String simpleName = toPascalCase(table.name()) + "Record";
        final String canonicalName = recordsPackageName + "." + simpleName;

        return new RecordCodeModel(
            table.name(),
            simpleName,
            canonicalName,
            recordsPackageName,
            unmodifiableList(primaryKeys),
            unmodifiableList(fields)
        );
    }

    @Nonnull
    private ExecutionCodeModel createExecutionCodeModel(
        @Nonnull final String executionsPackageName,
        @Nonnull final DatabaseMetadata.Table table,
        @Nonnull final RecordCodeModel recordCodeModel
    ) {
        final String simpleName = toPascalCase(table.name()) + "Execution";
        final String canonicalName = executionsPackageName + "." + simpleName;

        return new ExecutionCodeModel(
            table.name(),
            simpleName,
            canonicalName,
            executionsPackageName,
            recordCodeModel
        );
    }

    /**
     * Converts a string (e.g., snake_case, kebab-case, camelCase)
     * into PascalCase (e.g., "my_data" -> "MyData").
     * * @param name The input string to convert.
     *
     * @return The converted string in PascalCase format.
     */
    @Nonnull
    private String toPascalCase(@Nonnull final String name) {
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

    @Nonnull
    private String toCamelCase(@Nonnull final String name) {
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

    @Nonnull
    private <T> Class<T> getJavaType(@Nonnull final String type) {
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

    @Nonnull
    private String mapSqlTypeToJavaTypeCanonicalName(@Nonnull final String type) {
        final String normalizedType = type.toLowerCase().split("\\(")[0].trim();
        final String javaClass = switch (normalizedType) {
            // Numeric Types
            case "smallint", "int2" -> Short.class.getCanonicalName();
            case "integer", "int4" -> Integer.class.getCanonicalName();
            case "bigint", "int8", "serial", "bigserial" -> Long.class.getCanonicalName();
            case "real", "float4" -> Float.class.getCanonicalName();
            case "double precision", "float8" -> Double.class.getCanonicalName();
            case "numeric", "decimal" -> java.math.BigDecimal.class.getCanonicalName();

            // Character/String Types
            case "varchar", "character varying", "char", "character", "text" -> String.class.getCanonicalName();
            case "uuid" -> java.util.UUID.class.getCanonicalName();

            // Boolean Type
            case "boolean", "bool" -> Boolean.class.getCanonicalName();

            // Date/Time Types (Using Java 8+ time classes)
            case "date" -> java.time.LocalDate.class.getCanonicalName();
            case "time", "time without time zone" -> java.time.LocalTime.class.getCanonicalName();
            case "timestamp", "timestamp without time zone" -> java.sql.Timestamp.class.getCanonicalName();
            case "timestamptz", "timestamp with time zone" -> java.sql.Timestamp.class.getCanonicalName();
            case "timetz", "time with time zone" -> java.sql.Timestamp.class.getCanonicalName();

            // Binary/LOB Types
            case "bytea" -> byte[].class.getCanonicalName(); // Often mapped to byte array

            // Special/Other Types
            case "json", "jsonb" ->
                String.class.getCanonicalName(); // Often treated as a String, or a custom type/library object

            // Default Case for Unsupported or Unknown Types
            default -> throw new IllegalArgumentException("Unsupported PostgreSQL type: " + type);
        };

        return javaClass;
    }

    @Nonnull
    private RecordFieldCodeModel createRecordFieldCodeModel(
        @Nonnull final DatabaseMetadata.Column column,
        @Nonnull final DatabaseMetadata.Table table,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        final ColumnType columnConfiguration = getColumnConfiguration(table, column, xmlRepositoryConfiguration);
        String type = mapSqlTypeToJavaTypeCanonicalName(column.type());

        if (columnConfiguration != null && columnConfiguration.getJavaType() != null) {
            type = columnConfiguration.getJavaType();
        }

        final String name = toCamelCase(column.name());
        return new RecordFieldCodeModel(
            name,
            column.name(),
            type,
            column.nullable(),
            column.primaryKey()
        );
    }

    @Nullable
    private ColumnType getColumnConfiguration(
        @Nonnull final DatabaseMetadata.Table table,
        @Nonnull final DatabaseMetadata.Column column,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        for (final TableType tableConfiguration : xmlRepositoryConfiguration.getTables()) {
            if (!tableConfiguration.getName().equalsIgnoreCase(table.name())) {
                continue;
            }

            for (final ColumnType columnConfiguration : tableConfiguration.getColumns()) {
                if (columnConfiguration.getName().equalsIgnoreCase(column.name())) {
                    return columnConfiguration;
                }
            }
        }

        return null;
    }
}