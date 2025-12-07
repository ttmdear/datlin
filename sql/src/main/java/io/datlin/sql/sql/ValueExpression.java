package io.datlin.sql.sql;

import io.datlin.sql.ast.Node;

public final class ValueExpression implements Node
{

    final Object value;

    ValueExpression(Object value) {
        this.value = value;
    }
}
