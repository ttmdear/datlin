package io.datlin.sql.sql;

import com.clever4j.lang.AllNonnullByDefault;

@AllNonnullByDefault
public final class Column {

    final Expression value;
    final String alias;

    Column(Expression value, String alias) {
        this.value = value;
        this.alias = alias;
    }
}