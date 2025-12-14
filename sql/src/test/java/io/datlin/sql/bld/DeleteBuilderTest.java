package io.datlin.sql.bld;

import io.datlin.sql.ast.DeleteNode;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeleteBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final UUID uuid = UUID.fromString("9dfa1bd2-d868-11f0-8de9-0242ac120002");
        final DeleteNode delete = new DeleteBuilder()
            .table("public", "pls_plan")
            .where(where -> {
                where.eq("pls_plan", "plan_id", uuid);
            })
            .build();

        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(delete, sql, context);

        assertEquals("DELETE FROM \"public\".\"pls_plan\" WHERE (\"pls_plan\".\"plan_id\" = ?)", sql.toString());
        assertEquals(1, context.getStatementObjects().size());
        assertEquals(uuid, context.getStatementObject(0));
    }

}