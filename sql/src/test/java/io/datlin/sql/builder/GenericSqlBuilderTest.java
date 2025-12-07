package io.datlin.sql.builder;

import io.datlin.sql.expression.BinaryExpression;
import io.datlin.sql.expression.BinaryOperator;
import io.datlin.sql.expression.ColumnExpression;
import io.datlin.sql.expression.ColumnLiteralExpression;
import io.datlin.sql.expression.ConditionsExpression;
import io.datlin.sql.expression.FromExpression;
import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.expression.TableLiteralExpression;
import io.datlin.sql.expression.UuidValueExpression;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.datlin.sql.expression.LogicOperator.AND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericSqlBuilderTest {

    @Nonnull
    private final GenericSqlBuilder builder = new GenericSqlBuilder();

    @Test
    void buildSelect() {
        final SelectExpression select = new SelectExpression(
            List.of(
                new ColumnExpression(new ColumnLiteralExpression("p", "plan_id"), "plan_id"),
                new ColumnExpression(new ColumnLiteralExpression("p", "name"), "name")
            ),
            new FromExpression(new TableLiteralExpression("public", "pls_plan"), "p", List.of()),
            new ConditionsExpression(AND, List.of(
                new BinaryExpression(
                    new ColumnLiteralExpression("p", "plan_id"),
                    BinaryOperator.EQ,
                    new UuidValueExpression(UUID.fromString("b2c63d7c-d2b2-11f0-8de9-0242ac120002"))
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
        final SelectExpression select = new SelectExpression(
            List.of(
                new ColumnExpression(new ColumnLiteralExpression("p", "plan_id"), "plan_id"),
                new ColumnExpression(new ColumnLiteralExpression("p", "name"), "name")
            ),
            new FromExpression(new TableLiteralExpression("public", "pls_plan"), "p", List.of()),
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
        final SelectExpression select = new SelectExpression(
            List.of(
                new ColumnExpression(new ColumnLiteralExpression("p", "plan_id"), "plan_id"),
                new ColumnExpression(new ColumnLiteralExpression("p", "name"), "name")
            ),
            new FromExpression(new TableLiteralExpression("public", "pls_plan"), "p", List.of()),
            new  ConditionsExpression(AND, List.of())
        );

        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p",
            sql.toString());
    }
}