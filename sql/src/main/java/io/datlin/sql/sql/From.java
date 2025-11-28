package io.datlin.sql.sql;

import com.clever4j.lang.AllNonnullByDefault;

@AllNonnullByDefault
public final class From {

    final Expression value;
    final String alias;

    From(Expression value, String alias) {
        this.value = value;
        this.alias = alias;
    }
}