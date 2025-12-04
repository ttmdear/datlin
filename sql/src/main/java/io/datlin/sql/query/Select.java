package io.datlin.sql.query;

import io.datlin.sql.clause.From;
import io.datlin.sql.sql.Expression;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Select implements Expression {

    @Nonnull
    private final List<Column> columns = new ArrayList<>();

    @Nullable
    private final From from;

    public Select(
        @Nullable final From from
    ) {
        this.from = from;
    }

    // @Nullable
    // private final Where where;

    // @Nullable
    // private final OrderBy orderBy;

    // public static @Nonnull Select build() {
    //     return new Select();
    // }

    // // from ------------------------------------------------------------------------------------------------------------
    // public @Nonnull Select from(final @Nonnull From from) {
    //     this.from = from;
    //     return this;
    // }

    // // column ----------------------------------------------------------------------------------------------------------
    // public Select column(String column) {
    //     columns.add(new Column(Identifier.of(column), ""));
    //     return this;
    // }

    // public Select column(String column, String alias) {
    //     columns.add(new Column(Identifier.of(column), alias));
    //     return this;
    // }

    // public Select clearColumns() {
    //     columns.clear();
    //     return this;
    // }

    // // where -----------------------------------------------------------------------------------------------------------
    // public @Nonnull Where where() {
    //     if (where == null) {
    //         where = new Where(LogicOperator.AND);
    //     }

    //     return where;
    // }

    // public @Nonnull Select where(final @Nonnull WhereConfigurer configurer) {
    //     if (where == null) {
    //         where = new Where(LogicOperator.AND);
    //     }

    //     configurer.configure(where);
    //     return this;
    // }

    // // orderBy ---------------------------------------------------------------------------------------------------------
    // public @Nonnull OrderBy orderBy() {
    //     if (orderBy == null) {
    //         this.orderBy = new OrderBy();
    //     }

    //     return orderBy;
    // }

    // public @Nonnull Select orderBy(final @Nonnull OrderByConfigurer configurer) {
    //     if (orderBy == null) {
    //         this.orderBy = new OrderBy();
    //     }

    //     configurer.configure(orderBy);

    //     return this;
    // }

    // public @Nonnull Select orderBy(final @Nonnull OrderBy orderBy) {
    //     this.orderBy = orderBy;
    //     return this;
    // }

    public static class Column {
        final Expression value;
        final String alias;

        Column(Expression value, String alias) {
            this.value = value;
            this.alias = alias;
        }
    }
}
