package io.datlin.sql.builder;

import io.datlin.sql.exception.FromNotSetException;
import io.datlin.sql.expression.*;
import io.datlin.sql.expression.ConditionsExpressionBuilder.ConditionsExpressionConfigurer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SelectExpressionBuilder {

    @Nonnull
    private final List<ColumnExpression> columns = new ArrayList<>();

    @Nullable
    private FromExpression from;

    @Nonnull
    private final ConditionsExpressionBuilder whereBuilder = new ConditionsExpressionBuilder();

    @Nonnull
    public SelectExpressionBuilder column(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final String alias
    ) {
        this.columns.add(new ColumnExpression(new ColumnLiteralExpression(table, column), alias));
        return this;
    }

    @Nonnull
    public SelectExpressionBuilder from(
        @Nonnull String schema,
        @Nonnull String name,
        @Nonnull String alias
    ) {
        this.from = new FromExpression(new TableLiteralExpression(schema, name), alias, List.of());
        return this;
    }

    @Nonnull
    public SelectExpressionBuilder where(
        @Nonnull final ConditionsExpressionConfigurer configurer
    ) {
        configurer.configure(whereBuilder);
        return this;
    }

    @Nonnull
    public SelectExpression build() {
        if (this.from == null) {
            throw new FromNotSetException();
        }

        return new SelectExpression(columns, from, whereBuilder.build());
    }
}
