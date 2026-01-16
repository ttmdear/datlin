package io.datlin.sql.bld;

import io.datlin.sql.ast.PostgresUpsert;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.TableReference.table;

class PostgresUpsertGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void buildCase1() {
        final PostgresUpsert upsert = PostgresUpsert.upsert()
            .columns(
                column("plan_id"),
                column("name")
            )
            .into(table("plan"))
            .values(List.of(
                List.of(10, "plan 10")
            ))
            .onConflict(column("plan_id"))
            .doNothing(true);

        assertSql(
            "INSERT INTO \"plan\" (\"plan_id\", \"name\") VALUES (?, ?) ON CONFLICT (\"plan_id\") DO NOTHING",
            List.of(10, "plan 10"),
            sqlBuilder,
            upsert
        );
    }
}