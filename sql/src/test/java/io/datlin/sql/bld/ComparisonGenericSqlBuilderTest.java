package io.datlin.sql.bld;

import io.datlin.sql.ast.Comparison;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.ast.ColumnReference.column;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparisonGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void eq() {
        final Comparison comparison = Comparison.eq(column("c1").from("c1_from"), column("b"));
        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();
        sqlBuilder.build(comparison, sql, buildContext);

        assertEquals("\"c1_from\".\"c1\" = \"b\"", sql.toString());
    }
}