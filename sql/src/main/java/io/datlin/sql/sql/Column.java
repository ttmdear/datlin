package io.datlin.sql.sql;

public final class Column {

    final Expression value;
    final String alias;

    Column(Expression value, String alias) {
        this.value = value;
        this.alias = alias;
    }
}