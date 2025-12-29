package io.datlin.sql.bld;

import io.datlin.sql.SqlAssertions;
import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.Select;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.Comparison.eq;
import static io.datlin.sql.ast.Criteria.or;
import static io.datlin.sql.ast.TableReference.table;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void buildCase1() {
        final Select select = Select.select()
            .columns(
                column("c1").from("c1_from").as("c1_as"),
                column("c2").from("c2_from"),
                column("c3").as("c3_as"),
                column("c4")
            )
            .from(table("t1").schema("public").as("t1_as"))
            .where(
                Criteria.and(
                    or(
                        eq(column("cr1"), column("cr2").from("cr2_from")),
                        eq(column("cr3"), column("cr4"))
                    ),
                    eq(column("cr5"), column("cr6"))
                )
            );

        final StringBuilder sql = new StringBuilder();
        final BuildContext buildContext = new BuildContext();
        sqlBuilder.build(select, sql, buildContext);

        assertEquals("SELECT \"c1_from\".\"c1\" AS \"c1_as\", \"c2_from\".\"c2\", \"c3\" AS \"c3_as\", \"c4\" FROM \"public\".\"t1\" AS \"t1_as\" WHERE (\"cr1\" = \"cr2_from\".\"cr2\" OR \"cr3\" = \"cr4\") AND \"cr5\" = \"cr6\"", sql.toString());
        assertEquals(0, buildContext.getStatementObjects().size());
    }

    @Test
    void buildCase2() {
        final Select select = Select.select()
            .from(table("t1").schema("public").as("t1_as"))
            .where(eq(column("cr5"), "cr5_value"));

        assertSql(
            "SELECT * FROM \"public\".\"t1\" AS \"t1_as\" WHERE \"cr5\" = ?",
            List.of("cr5_value"),
            sqlBuilder,
            select
        );
    }
}