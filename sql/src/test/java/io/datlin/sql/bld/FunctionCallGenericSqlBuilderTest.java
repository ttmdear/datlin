package io.datlin.sql.bld;

import io.datlin.sql.ast.FunctionCall;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.SqlAssertions.assertSql;

class FunctionCallGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        assertSql("COUNT(*)", sqlBuilder, FunctionCall.countAll());
    }
}