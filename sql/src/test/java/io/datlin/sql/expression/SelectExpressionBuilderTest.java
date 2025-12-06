package io.datlin.sql.expression;

import io.datlin.sql.builder.GenericSqlBuilder;
import io.datlin.sql.builder.SelectExpressionBuilder;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectExpressionBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final SelectExpression select = new SelectExpressionBuilder()
            .column("p", "plan_id", "plan_id")
            .from("public", "pls_plan", "p")
            .where(builder -> {
                builder.eq("p", "plan_id", UUID.fromString("9747e23a-e664-405e-9e3a-0c23e1c42c57"));
            })
            .build();

        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();
        sqlBuilder.build(select, sql, buildContext);

        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id FROM \"public\".\"pls_plan\" AS p WHERE (\"p\".\"plan_id\" = ?)", sql.toString());

        assertEquals(1, buildContext.getStatementObjects().size());
        assertEquals(UUID.fromString("9747e23a-e664-405e-9e3a-0c23e1c42c57"), buildContext.getStatementObject(0));
    }
}