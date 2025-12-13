package io.datlin.sql.bld;

import io.datlin.sql.ast.UpdateNode;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final UUID uuid = UUID.fromString("9dfa1bd2-d868-11f0-8de9-0242ac120002");
        final UpdateNode update = new UpdateBuilder()
            .table("public", "pls_plan")
            .set("plan_id", "1")
            .set("plan_name", "test plan")
            .where(builder -> {
                builder.eq("pls_plan", "plan_id", uuid);
            }).build();

        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(update, sql, context);

        assertEquals("UPDATE \"public\".\"pls_plan\" SET \"plan_id\" = ?, \"plan_name\" = ? WHERE (\"pls_plan\".\"plan_id\" = ?)", sql.toString());
        assertEquals("1", context.getStatementObject(0));
        assertEquals("test plan", context.getStatementObject(1));
        assertEquals(uuid, context.getStatementObject(2));
    }
}