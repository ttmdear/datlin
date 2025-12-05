package io.datlin.sql.clause;

import io.datlin.sql.expression.SelectExpression;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class From {

    @Nullable
    private TableSource tableSource;

    @Nullable
    private SelectSource selectSource;

    @Nonnull
    private final String alias;

    public From(
        @Nonnull final TableSource tableSource,
        @Nonnull final String alias
    ) {
        this.tableSource = tableSource;
        this.alias = alias;
    }

    public From(
        @Nonnull final SelectSource selectSource,
        @Nonnull final String alias
    ) {
        this.selectSource = selectSource;
        this.alias = alias;
    }

    @Nullable
    public TableSource getTableSource() {
        return tableSource;
    }

    @Nullable
    public SelectSource getSelectSource() {
        return selectSource;
    }

    @Nonnull
    public String getAlias() {
        return alias;
    }

    public static class TableSource {

        @Nonnull
        private final String table;

        public TableSource(
            @Nonnull final String table
        ) {
            this.table = table;
        }
    }

    public static class SelectSource {

        @Nonnull
        private final SelectExpression select;

        public SelectSource(@Nonnull final SelectExpression select) {
            this.select = select;
        }
    }
}