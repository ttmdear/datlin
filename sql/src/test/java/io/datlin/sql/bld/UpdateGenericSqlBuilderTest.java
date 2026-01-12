package io.datlin.sql.bld;

import io.datlin.sql.ast.Assignment;
import io.datlin.sql.ast.BinaryExpression;
import io.datlin.sql.ast.Update;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.Criteria.or;
import static io.datlin.sql.ast.TableReference.table;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void buildCase1() {
        final Update update = Update.update()
            .table(table("pls_plan").schema("public").as("t"))
            .sets(
                Assignment.set(column("name"), "plan")
            )
            .where(
                BinaryExpression.eq(column("id"), 100)
            );

        assertSql(
            "UPDATE \"pls_plan\" SET \"name\" = ? WHERE \"id\" = 100",
            sqlBuilder,
            update
        );
    }
}