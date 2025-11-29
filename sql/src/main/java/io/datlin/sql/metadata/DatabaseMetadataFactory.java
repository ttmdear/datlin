package io.datlin.sql.metadata;

import io.datlin.sql.metadata.DatabaseMetadata.Column;
import io.datlin.sql.metadata.DatabaseMetadata.Table;
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
    public @Nonnull DatabaseMetadata create(
        final @Nonnull Connection connection
    ) throws SQLException {
        return new DatabaseMetadata(
            getTables(connection)
        );
    }

    private @Nonnull List<Table> getTables(
        final @Nonnull Connection connection
    ) throws SQLException {
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        final ResultSet tablesResultSet = databaseMetaData.getTables(
            connection.getCatalog(),
            null,
            "%",
            new String[]{"TABLE"}
        );

        final List<Table> tables = new ArrayList<>();
        while (tablesResultSet.next()) {
            final String tableName = tablesResultSet.getString("TABLE_NAME");
            final List<Column> columns = getColumns(tableName, databaseMetaData);
            tables.add(new Table(tableName, Collections.unmodifiableList(columns)));
        }

        return tables;
    }

    private @Nonnull List<Column> getColumns(
        final @Nonnull String tableName,
        final @Nonnull DatabaseMetaData databaseMetaData
    ) throws SQLException {
        final ResultSet columnsResultSet = databaseMetaData.getColumns(
            null,
            null,
            tableName,
            null
        );

        final List<Column> columns = new ArrayList<>();
        final Set<String> primaryKeys = getPrimaryKeys(tableName, databaseMetaData);

        while (columnsResultSet.next()) {
            final String name = columnsResultSet.getString("COLUMN_NAME");
            final String type = columnsResultSet.getString("TYPE_NAME");
            final boolean primaryKey = primaryKeys.contains(name);
            final boolean isNullable = columnsResultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

            columns.add(new Column(name, primaryKey, type, isNullable));
        }

        return columns;
    }

    private @Nonnull Set<String> getPrimaryKeys(
        final @Nonnull String tableName,
        final @Nonnull DatabaseMetaData metaData
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