package io.datlin.sql.sql;

import java.util.List;

import io.datlin.sql.ast.Node;

public final class ValuesExpression implements Node
{
    final List<?> values;

    ValuesExpression(List<?> values) {
        this.values = values;
    }
}
