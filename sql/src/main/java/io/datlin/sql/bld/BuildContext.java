package io.datlin.sql.bld;

import jakarta.annotation.Nonnull;

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
}