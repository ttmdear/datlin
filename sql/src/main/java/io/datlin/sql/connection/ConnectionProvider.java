package io.datlin.sql.connection;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection(String databaseId);

    Connection getNewConnection(String databaseId);
}