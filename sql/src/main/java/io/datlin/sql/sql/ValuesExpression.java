package io.datlin.sql.sql;

import com.clever4j.lang.AllNonnullByDefault;

import java.util.List;

@AllNonnullByDefault
public final class ValuesExpression implements Expression {

    final List<?> values;

    ValuesExpression(List<?> values) {
        this.values = values;
    }
}
