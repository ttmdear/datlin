package io.datlin.sql.bld;

import io.datlin.sql.ast.InsertNode;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsertBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final UUID e1 = UUID.fromString("b6f8f18a-d6c9-11f0-8de9-0242ac120002");
        final UUID e2 = UUID.fromString("4706b64c-d6cd-11f0-8de9-0242ac120002");

        final InsertNode insert = new InsertBuilder()
            .into("public", "pls_plans")
            .fields("id", "name", "age")
            .values(e1, "record1", 10)
            .values(List.of(e2, "record2", 20))
            .build();

        final StringBuilder sql = new StringBuilder();
        final BuildContext context = new BuildContext();

        sqlBuilder.build(insert, sql, context);

        assertEquals("INSERT INTO \"public\".\"pls_plans\" (id, name, age) VALUES (?, ?, ?),  (?, ?, ?)", sql.toString());
        assertEquals(6, context.getStatementObjects().size());
        assertEquals(e1, context.getStatementObject(0));
        assertEquals("record1", context.getStatementObject(1));
        assertEquals(10, context.getStatementObject(2));
        assertEquals(e2, context.getStatementObject(3));
        assertEquals("record2", context.getStatementObject(4));
        assertEquals(20, context.getStatementObject(5));
    }
}