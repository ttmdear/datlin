package io.datlin.sql.sql;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Select implements Expression {

    List<Column> columns = new ArrayList<>();

    @Nullable
    From from;

    @Nullable
    Where where;

    @Nullable
    OrderBy orderBy;

    Select() {

    }

    public static Select build() {
        return new Select();
    }

    // column ----------------------------------------------------------------------------------------------------------
    public Select column(String column) {
        columns.add(new Column(Identifier.of(column), ""));
        return this;
    }

    public Select column(String column, String alias) {
        columns.add(new Column(Identifier.of(column), alias));
        return this;
    }

    public Select clearColumns() {
        columns.clear();
        return this;
    }

    // from ------------------------------------------------------------------------------------------------------------
    public Select from(String identifier) {
        this.from = new From(Identifier.of(identifier), "");
        return this;
    }

    public Select from(String identifier, String alias) {
        this.from = new From(Identifier.of(identifier), alias);
        return this;
    }

    // where -----------------------------------------------------------------------------------------------------------
    public Where where() {
        if (where == null) {
            where = new Where(LogicOperator.AND);
        }

        return where;
    }

    public Select where(Where.WhereConfigurer configurer) {
        if (where == null) {
            where = new Where(LogicOperator.AND);
        }

        configurer.configure(where);

        return this;
    }

    // orderBy ---------------------------------------------------------------------------------------------------------
    public OrderBy orderBy() {
        if (orderBy == null) {
            this.orderBy = new OrderBy();
        }

        return orderBy;
    }

    public Select orderBy(OrderBy.OrderByConfigurer configurer) {
        if (orderBy == null) {
            this.orderBy = new OrderBy();
        }

        configurer.configure(orderBy);

        return this;
    }

    public Select orderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
        return this;
    }
}
