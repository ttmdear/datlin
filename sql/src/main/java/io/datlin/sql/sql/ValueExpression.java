package io.datlin.sql.sql;

import io.datlin.sql.expression.Expression;

public final class ValueExpression implements Expression
{

    final Object value;

    ValueExpression(Object value) {
        this.value = value;
    }
}
