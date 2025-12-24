package io.datlin.sql.bld;

import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.TableReference;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.SqlAssertions.assertSql;
import static io.datlin.sql.ast.Criteria.or;
import static io.datlin.sql.ast.TableReference.table;
import static io.datlin.sql.ast.ValueReference.value;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueReferenceGenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        assertSql("20", sqlBuilder, value(20L));
        assertSql("20.2", sqlBuilder, value(20.20d));
        assertSql("30.2", sqlBuilder, value(30.20f));
        assertSql("40", sqlBuilder, value(40));
        assertSql("50", sqlBuilder, value((short) 50));
        assertSql("20", sqlBuilder, value(20));
        assertSql("?", sqlBuilder, value(new Object()));
    }

    @Test
    void buildComparison() {
        final Select select30 = Select.select()
            .columns(
                ColumnReference.column("user_id")
            )
            .from(table("plans"));

        assertSql("20 = (SELECT \"user_id\" FROM \"plans\")", sqlBuilder, value(20L).eq(select30));

        assertSql("20 = 30", sqlBuilder, value(20L).eq(30));
    }
}