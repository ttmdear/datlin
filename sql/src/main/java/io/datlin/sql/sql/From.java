package io.datlin.sql.sql;

public final class From {
    final Expression value;
    final String alias;

    From(Expression value, String alias) {
        this.value = value;
        this.alias = alias;
    }
}