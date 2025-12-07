package io.datlin.sql.bld;

import io.datlin.sql.ast.ComparisonNode;
import io.datlin.sql.ast.ColumnNode;
import io.datlin.sql.ast.ColumnLiteralNode;
import io.datlin.sql.ast.ComparisonOperator;
import io.datlin.sql.ast.LogicalNode;
import io.datlin.sql.ast.FromNode;
import io.datlin.sql.ast.SelectNode;
import io.datlin.sql.ast.TableLiteralNode;
import io.datlin.sql.ast.UuidValueNode;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.datlin.sql.ast.LogicalOperator.AND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder builder = new GenericSqlBuilder();

    @Test
    void buildSelect() {
        final SelectNode select = new SelectNode(
            List.of(
                new ColumnNode(new ColumnLiteralNode("p", "plan_id"), "plan_id"),
                new ColumnNode(new ColumnLiteralNode("p", "name"), "name")
            ),
            new FromNode(new TableLiteralNode("public", "pls_plan"), "p", List.of()),
            new LogicalNode(AND, List.of(
                new ComparisonNode(
                    new ColumnLiteralNode("p", "plan_id"),
                    ComparisonOperator.EQ,
                    new UuidValueNode(UUID.fromString("b2c63d7c-d2b2-11f0-8de9-0242ac120002"))
                )
            ))
        );

        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p " +
            "WHERE (\"p\".\"plan_id\" = ?)", sql.toString());
        assertEquals(1, buildContext.getStatementObjects().size());
        assertEquals(UUID.fromString("b2c63d7c-d2b2-11f0-8de9-0242ac120002"), buildContext.getStatementObject(0));
    }

    @Test
    void buildSelectNoWhere() {
        final SelectNode select = new SelectNode(
            List.of(
                new ColumnNode(new ColumnLiteralNode("p", "plan_id"), "plan_id"),
                new ColumnNode(new ColumnLiteralNode("p", "name"), "name")
            ),
            new FromNode(new TableLiteralNode("public", "pls_plan"), "p", List.of()),
            null
        );

        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p",
            sql.toString());
    }

    @Test
    void buildSelectEmptyWhere() {
        final SelectNode select = new SelectNode(
            List.of(
                new ColumnNode(new ColumnLiteralNode("p", "plan_id"), "plan_id"),
                new ColumnNode(new ColumnLiteralNode("p", "name"), "name")
            ),
            new FromNode(new TableLiteralNode("public", "pls_plan"), "p", List.of()),
            new LogicalNode(AND, List.of())
        );

        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p",
            sql.toString());
    }
}