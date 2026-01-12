package io.datlin.sql.bld;

import io.datlin.sql.ast.InExpression;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.ColumnReference.column;

class InExpressionGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void buildIn() {
        assertSql(
            "\"plan_id\" IN (1, 2)",
            sqlBuilder,
            InExpression.in(
                column("plan_id"),
                List.of(1, 2)
            )
        );
    }
}