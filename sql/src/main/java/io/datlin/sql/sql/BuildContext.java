package io.datlin.sql.sql;

import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildContext {

    @Nonnull
    private final List<Object> statementObjects = new ArrayList<>();

    public void addStatementObjects(@Nonnull final Object value) {
        this.statementObjects.add(value);
    }

    @Nonnull
    public List<Object> getStatementObjects() {
        return statementObjects;
    }

    @Nonnull
    public Object getStatementObject(int index) {
        return statementObjects.get(index);
    }

    public void prepareStatement(@Nonnull final PreparedStatement statement) {
        try {
            for (int i = 0; i < statementObjects.size(); i++) {
                statement.setObject(i + 1, statementObjects.get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}