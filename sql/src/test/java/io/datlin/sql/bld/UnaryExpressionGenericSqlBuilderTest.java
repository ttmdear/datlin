package io.datlin.sql.bld;

import io.datlin.sql.ast.BinaryExpression;
import io.datlin.sql.ast.UnaryExpression;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.ColumnReference.column;

class UnaryExpressionGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void buildIsNull() {
        assertSql(
            "\"plan_id\" IS NULL",
            sqlBuilder,
            UnaryExpression.isNull(column("plan_id"))
        );
    }

    @Test
    void buildIsNotNull() {
        assertSql(
            "\"plan_id\" IS NOT NULL",
            sqlBuilder,
            UnaryExpression.isNotNull(column("plan_id"))
        );
    }

    @Test
    void buildNot() {
        assertSql(
            "NOT (\"plan_id\" = \"user_id\")",
            sqlBuilder,
            UnaryExpression.not(BinaryExpression.eq(column("plan_id"), column("user_id")))
        );
    }
}