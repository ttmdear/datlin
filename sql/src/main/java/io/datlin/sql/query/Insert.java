package io.datlin.sql.query;

import io.datlin.sql.expression.Expression;
import io.datlin.sql.sql.Identifier;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Insert implements Expression {

    @Nullable
    Identifier into;

    List<String> columns = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    Insert() {

    }

    public static Insert build() {
        return new Insert();
    }

    // into ------------------------------------------------------------------------------------------------------------
    public Insert into(String into) {
        this.into = Identifier.of(into);
        return this;
    }

    // value -----------------------------------------------------------------------------------------------------------
    public <T> Insert value(String column, T value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    public Insert clear() {
        columns.clear();
        values.clear();
        return this;
    }
}