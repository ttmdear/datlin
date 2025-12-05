package io.datlin.sql.sql;

import java.util.List;

import io.datlin.sql.expression.Expression;

public final class ValuesExpression implements Expression
{
    final List<?> values;

    ValuesExpression(List<?> values) {
        this.values = values;
    }
}
