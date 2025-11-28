package io.datlin.sql.sql;

import java.util.List;

public final class ValuesExpression implements Expression {
    final List<?> values;

    ValuesExpression(List<?> values) {
        this.values = values;
    }
}
