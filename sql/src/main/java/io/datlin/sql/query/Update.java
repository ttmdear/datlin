package io.datlin.sql.query;

import io.datlin.sql.clause.Where;
import io.datlin.sql.expression.Expression;
import io.datlin.sql.sql.Identifier;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Update implements Expression {

    @Nullable
    Identifier table;

    List<String> columns = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    @Nullable
    Where where;

    Update() {

    }

    public static Update build() {
        return new Update();
    }

    // table ------------------------------------------------------------------------------------------------------------
    public Update table(String table) {
        this.table = Identifier.of(table);
        return this;
    }

    // set -----------------------------------------------------------------------------------------------------------
    public <T> Update set(String column, T value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    // where -----------------------------------------------------------------------------------------------------------
    public Where where() {
//        this.where = new Where(LogicOperator.AND);
//        return this.where;

        return null;
    }

    public Update where(Where.WhereConfigurer configurer) {
//        if (where == null) {
//            where = new Where(LogicOperator.AND);
//        }
//
//        configurer.configure(where);
//
//        return this;
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public Update clear() {
        columns.clear();
        values.clear();
        return this;
    }
}