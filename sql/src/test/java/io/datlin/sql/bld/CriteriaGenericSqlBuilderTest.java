package io.datlin.sql.bld;

import io.datlin.sql.ast.Criteria;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.BinaryExpression.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CriteriaGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final Criteria criteria = Criteria.and(
            eq(column("c1").from("c1_from"), column("c2")),
            Criteria.or(
                eq(column("c3"), column("c4")),
                eq(column("c5"), column("c6"))
            ),
            Criteria.and(
                eq(column("c7"), column("c8")),
                eq(column("c9"), column("c10"))
            )
        );

        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();
        sqlBuilder.build(criteria, sql, buildContext);

        assertEquals("\"c1_from\".\"c1\" = \"c2\" AND (\"c3\" = \"c4\" OR \"c5\" = \"c6\") AND (\"c7\" = \"c8\" AND \"c9\" = \"c10\")", sql.toString());
    }
}