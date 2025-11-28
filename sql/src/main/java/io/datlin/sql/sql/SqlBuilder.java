package io.datlin.sql.sql;

public interface SqlBuilder {
    String build(Expression expression, BuildContext context);
}
