package io.datlin.sql.sql;

public final class ValueExpression implements Expression {

    final Object value;

    ValueExpression(Object value) {
        this.value = value;
    }
}
