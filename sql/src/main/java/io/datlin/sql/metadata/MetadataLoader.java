package io.datlin.sql.metadata;

import com.clever4j.lang.AllNonnullByDefault;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllNonnullByDefault
public final class MetadataLoader {

    private final Connection connection;

    public MetadataLoader(Connection connection) {
        this.connection = connection;
    }

    public DatabaseMetadata load() throws SQLException {
        return new DatabaseMetadata(getTables());
    }

    public List<TableMetadata> getTables() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet resultSet = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
        List<TableMetadata> tableMetadata = new ArrayList<>();

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            List<ColumnMetadata> columns = getColumns(tableName, metaData);

            tableMetadata.add(new TableMetadata(tableName, Engine.POSTGRESQL, Collections.unmodifiableList(columns)));
        }

        return tableMetadata;
    }

    private List<ColumnMetadata> getColumns(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
        List<ColumnMetadata> columnMetadata = new ArrayList<>();

        Set<String> primaryKeys = getPrimaryKeys(tableName, metaData);

        while (resultSet.next()) {
            String name = resultSet.getString("COLUMN_NAME");
            String type = resultSet.getString("TYPE_NAME");

            boolean primaryKey = primaryKeys.contains(name);
            boolean isNullable = resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;

            columnMetadata.add(new ColumnMetadata(name, primaryKey, type, isNullable));
        }

        return columnMetadata;
    }

    private Set<String> getPrimaryKeys(String tableName, DatabaseMetaData metaData) throws SQLException {
        ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName);
        Set<String> primaryKeys = new HashSet<>();

        while (resultSet.next()) {
            String pkColumnName = resultSet.getString("COLUMN_NAME");
            primaryKeys.add(pkColumnName);
        }

        return primaryKeys;
    }
}