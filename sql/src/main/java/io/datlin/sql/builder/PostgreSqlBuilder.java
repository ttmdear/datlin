package io.datlin.sql.builder;

import io.datlin.sql.query.Select;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

import java.util.List;

public class PostgreSqlBuilder implements SqlBuilder {

    @Override
    public void build(
        @Nonnull final Select select,
        @Nonnull final StringBuilder sql,
        @Nonnull final BuildContext context
    ) {
        sql.append("SELECT ");

        final List<Select.Column> columns = select.getColumns();

        if (columns.isEmpty()) {
            sql.append("*");
        } else {
            for (int i = 0; i < columns.size(); i++) {
                final Select.Column column = columns.get(i);
            }
        }
    }

    // p.pls_plan
}