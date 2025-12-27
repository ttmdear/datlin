package io.datlin.sql.mtd;

import jakarta.annotation.Nonnull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseMetadataFactory {

    @Nonnull
    public DatabaseMetadata create(
        @Nonnull final Connection connection
    ) throws SQLException {
        return new DatabaseMetadata(
            getTables(connection)
        );
    }

    @Nonnull
    private List<TableMetadata> getTables(
        @Nonnull final Connection connection
    ) throws SQLException {
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        final ResultSet tablesResultSet = databaseMetaData.getTables(
            connection.getCatalog(),
            null,
            "%",
            new String[]{"TABLE"}
        );

        final List<TableMetadata> tables = new ArrayList<>();
        while (tablesResultSet.next()) {
            final String tableName = tablesResultSet.getString("TABLE_NAME");
            final List<ColumnMetadata> columns = getColumns(tableName, databaseMetaData);
            tables.add(new TableMetadata(tableName, Collections.unmodifiableList(columns)));
        }

        return tables;
    }

    @Nonnull
    private List<ColumnMetadata> getColumns(
        @Nonnull final String tableName,
        @Nonnull final DatabaseMetaData databaseMetaData
    ) throws SQLException {
        final ResultSet columnsResultSet = databaseMetaData.getColumns(null, null, tableName, null);
        final List<ColumnMetadata> columns = new ArrayList<>();
        final Set<String> primaryKeys = getPrimaryKeys(tableName, databaseMetaData);

        while (columnsResultSet.next()) {
            final String name = columnsResultSet.getString("COLUMN_NAME");
            final String type = columnsResultSet.getString("TYPE_NAME");
            final boolean primaryKey = primaryKeys.contains(name);
            final boolean nullable = columnsResultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
            final int size = columnsResultSet.getInt("COLUMN_SIZE");
            final int decimalDigits = columnsResultSet.getInt("DECIMAL_DIGITS");

            Integer length = null;
            Integer precision = null;
            Integer scale = null;

            final ColumnAttributeType columnAttributeType = getColumnAttributeType(type);

            if (columnAttributeType.equals(ColumnAttributeType.LENGTH)) {
                length = size;
            } else if (columnAttributeType.equals(ColumnAttributeType.PRECISION_SCALE)) {
                precision = size;
                scale = decimalDigits;
            }

            columns.add(new ColumnMetadata(name, primaryKey, type, nullable, size, decimalDigits, length, precision,
                scale));
        }

        return columns;
    }

    public enum ColumnAttributeType {
        LENGTH, PRECISION_SCALE, NONE
    }

    @Nonnull
    public static ColumnAttributeType getColumnAttributeType(@Nonnull final String typeName) {
        final String type = typeName.toUpperCase();

        if (type.contains("VARCHAR") || type.equals("CHAR") || type.equals("TEXT")) {
            return ColumnAttributeType.LENGTH;
        }

        if (type.equals("DECIMAL") || type.equals("NUMERIC") ||
            type.equals("DOUBLE") || type.equals("FLOAT") || type.equals("REAL")) {
            return ColumnAttributeType.PRECISION_SCALE;
        }

        return ColumnAttributeType.NONE;
    }

    @Nonnull
    private Set<String> getPrimaryKeys(
        @Nonnull final String tableName,
        @Nonnull final DatabaseMetaData metaData
    ) throws SQLException {
        final ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName);
        final Set<String> primaryKeys = new HashSet<>();

        while (resultSet.next()) {
            final String pkColumnName = resultSet.getString("COLUMN_NAME");
            primaryKeys.add(pkColumnName);
        }

        return primaryKeys;
    }
}