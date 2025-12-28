package io.datlin.rcm;

import io.datlin.sql.mtd.ColumnMetadata;
import io.datlin.sql.mtd.DatabaseMetadata;
import io.datlin.sql.mtd.TableMetadata;
import io.datlin.sql.rsp.DefaultResultSetProcessor;
import io.datlin.xrc.generated.ColumnType;
import io.datlin.xrc.generated.GenerateTableStrategy;
import io.datlin.xrc.generated.ResultSetProcessorType.TypeBinding;
import io.datlin.xrc.generated.TableType;
import io.datlin.xrc.generated.XmlRepositoryConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class RepositoryCodeModelFactory {

    @Nonnull
    private final XmlRepositoryConfiguration xrc;

    @Nonnull
    private final DatabaseMetadata databaseMetadata;

    public RepositoryCodeModelFactory(
        @Nonnull final XmlRepositoryConfiguration xrc,
        @Nonnull final DatabaseMetadata databaseMetadata
    ) {
        this.xrc = xrc;
        this.databaseMetadata = databaseMetadata;
    }

    @Nonnull
    public RepositoryCodeModel create() {
        final String repositoryPackageName = xrc.getPackage();
        final String recordsPackageName = repositoryPackageName + ".records";
        final String tablesPackageName = repositoryPackageName + ".tables";
        final String executionsPackageName = repositoryPackageName + ".executions";

        final RepositoryCodeModel repository = new RepositoryCodeModel(
            repositoryPackageName,
            recordsPackageName,
            tablesPackageName,
            executionsPackageName
        );

        // database code model -----------------------------------------------------------------------------------------
        final String simpleName = xrc.getSimpleName();

        final DatabaseCodeModel database = new DatabaseCodeModel(
            simpleName,
            xrc.getPackage() + "." + simpleName,
            xrc.getPackage(),
            repository
        );

        repository.database = database;

        for (final TableMetadata tableMetadata : databaseMetadata.tables()) {
            if (isTableExcluded(tableMetadata, xrc)) {
                continue;
            }

            final TableCodeModel table = createTable(
                tablesPackageName,
                database,
                tableMetadata
            );

            repository.tables.add(table);

            // create record code model --------------------------------------------------------------------------------
            final RecordCodeModel record = createRecord(
                recordsPackageName,
                tableMetadata,
                table
            );

            repository.records.add(record);

            final String resultSetProcessor = resolveTableResultSetProcessor(tableMetadata, xrc);

            // create execution code model -----------------------------------------------------------------------------
            final ExecutionCodeModel execution = createExecution(
                executionsPackageName,
                table,
                tableMetadata,
                resultSetProcessor,
                record
            );

            repository.executions.add(execution);
            repository.database.executions.add(execution);
        }

        return repository;
    }

    private boolean isTableExcluded(
        @Nonnull final TableMetadata table,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        if (xmlRepositoryConfiguration.getGenerateTableStrategy().equals(GenerateTableStrategy.ALL)) {
            for (final TableType configurationTable : xmlRepositoryConfiguration.getTables()) {
                if (configurationTable.getName().equals(table.name()) && configurationTable.isExclude()) {
                    return true;
                }
            }

            return false;
        } else if (xmlRepositoryConfiguration.getGenerateTableStrategy().equals(GenerateTableStrategy.ONLY_DEFINED)) {
            for (final TableType configurationTable : xmlRepositoryConfiguration.getTables()) {
                if (configurationTable.getName().equals(table.name()) && !configurationTable.isExclude()) {
                    return false;
                }
            }

            return true;
        }

        return true;
    }

    @Nonnull
    private TableCodeModel createTable(
        @Nonnull final String tablesPackageName,
        @Nonnull final DatabaseCodeModel database,
        @Nonnull final TableMetadata metadata
    ) {
        final String simpleName = toPascalCase(metadata.name()) + "Table";
        final String canonicalName = tablesPackageName + "." + simpleName;
        final String tableReferenceField = metadata.name();

        final TableCodeModel table = new TableCodeModel(simpleName, canonicalName, tablesPackageName,
            tableReferenceField, metadata, database);

        for (final ColumnMetadata columnMetadata : metadata.columns()) {
            final TableColumnCodeModel column = new TableColumnCodeModel(
                columnMetadata.name(),
                columnMetadata.nullable(),
                columnMetadata,
                table
            );

            table.columns.add(column);
        }

        return table;
    }

    @Nonnull
    private RecordCodeModel createRecord(
        @Nonnull final String recordsPackageName,
        @Nonnull final TableMetadata tableMetadata,
        @Nonnull final TableCodeModel table
    ) {
        final String simpleName = toPascalCase(tableMetadata.name()) + "Record";
        final String canonicalName = recordsPackageName + "." + simpleName;

        final RecordCodeModel record = new RecordCodeModel(
            simpleName,
            canonicalName,
            recordsPackageName,
            table
        );

        for (final TableColumnCodeModel column : table.getColumns()) {
            record.fields.add(createRecordField(
                record,
                column.metadata,
                tableMetadata
            ));
        }

        record.fields.stream()
            .filter(field -> field.primaryKey)
            .forEach(record.primaryKeys::add);

        return record;
    }

    @Nonnull
    private ExecutionCodeModel createExecution(
        @Nonnull final String executionsPackageName,
        @Nonnull final TableCodeModel table,
        @Nonnull final TableMetadata tableMetadata,
        @Nonnull final String resultSetProcessor,
        @Nonnull final RecordCodeModel record
    ) {
        final String simpleName = toPascalCase(tableMetadata.name()) + "Execution";
        final String methodName = toCamelCase(tableMetadata.name());
        final String canonicalName = executionsPackageName + "." + simpleName;

        return new ExecutionCodeModel(
            simpleName,
            canonicalName,
            executionsPackageName,
            methodName,
            resultSetProcessor,
            table,
            record
        );
    }

    /**
     * Converts a string (e.g., snake_case, kebab-case, camelCase)
     * into PascalCase (e.g., "my_data" -> "MyData").
     * @param name The input string to convert.
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

    /**
     * Resolves the canonical name of the Java type as a {@link String} for the given database type.
     * <p>
     * The resolution follows this priority order:
     * 1. If an expected type is provided (suggested), it is used first.
     * 2. If no suggestion is available, the method checks the result-set-processor definitions.
     * 3. If still not found, the type is retrieved from the default configuration.
     * 3. If still not found, the type {@link Object} is returned.
     *
     * @param databaseType     the database-specific type to be resolved.
     * @param expectedJavaType an optional suggested Java type (takes precedence if provided).
     * @return the canonical name of the resolved Java type
     */
    @Nonnull
    private String resolveJavaType(
        @Nonnull final String databaseType,
        @Nullable final String expectedJavaType
    ) {
        if (expectedJavaType != null) {
            return expectedJavaType;
        }

        @Nullable final TypeBinding typeBinding = xrc.getResultSetProcessor().getTypeBindings().stream()
            .filter(typeBinding1 -> typeBinding1.getDatabaseType().equals(databaseType))
            .findFirst().orElse(null);

        if (typeBinding != null) {
            return typeBinding.getJavaType();
        }

        final String normalizedType = databaseType.toLowerCase().split("\\(")[0].trim();
        return switch (normalizedType) {
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
            default -> Object.class.getCanonicalName();
        };
    }

    /**
     * Create {@link RecordCodeModel} for given column.
     *
     * @param columnMetadata the column from {@link DatabaseMetadata}.
     * @param tableMetadata  the form {@link DatabaseMetadata}.
     * @return the {@link RecordCodeModel}.
     */
    @Nonnull
    private RecordFieldCodeModel createRecordField(
        @Nonnull final RecordCodeModel record,
        @Nonnull final ColumnMetadata columnMetadata,
        @Nonnull final TableMetadata tableMetadata
    ) {
        final ColumnType cc = resolveColumnConfiguration(tableMetadata, columnMetadata, xrc);
        final String databaseType = (cc != null && cc.getDatabaseType() != null) ? cc.getDatabaseType() : columnMetadata.type();
        final String javaType = resolveJavaType(databaseType, cc != null ? cc.getJavaType() : null);
        final String name = toCamelCase(columnMetadata.name());

        return new RecordFieldCodeModel(
            name,
            javaType,
            columnMetadata.nullable(),
            columnMetadata.primaryKey(),
            record,
            columnMetadata
        );
    }

    @Nullable
    private ColumnType resolveColumnConfiguration(
        @Nonnull final TableMetadata table,
        @Nonnull final ColumnMetadata column,
        @Nonnull final XmlRepositoryConfiguration xrc
    ) {
        for (final TableType tableConfiguration : xrc.getTables()) {
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

    @Nonnull
    private String resolveTableResultSetProcessor(
        @Nonnull final TableMetadata table,
        @Nonnull final XmlRepositoryConfiguration xmlRepositoryConfiguration
    ) {
        final TableType tableType = xmlRepositoryConfiguration.getTables().stream()
            .filter(tableType1 -> tableType1.getName().equalsIgnoreCase(table.name()))
            .findFirst().orElse(null);

        if (tableType == null || tableType.getResultSetProcessor() == null) {
            return DefaultResultSetProcessor.class.getCanonicalName();
        } else {
            return tableType.getResultSetProcessor();
        }
    }
}