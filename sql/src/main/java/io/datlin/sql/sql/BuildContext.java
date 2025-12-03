package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildContext {
    private final @Nonnull List<Object> statementObjects = new ArrayList<>();

    public void addStatementObjects(final @Nonnull Object value) {
        this.statementObjects.add(value);
    }

    public @Nonnull List<Object> getStatementObjects() {
        return statementObjects;
    }

    public void prepareStatement(final @Nonnull PreparedStatement statement) {
        try {
            for (int i = 0; i < statementObjects.size(); i++) {
                statement.setObject(i + 1, statementObjects.get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}