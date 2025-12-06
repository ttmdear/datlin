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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.datlin.sql.expression.LogicOperator.AND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericSqlBuilderTest {

    private final GenericSqlBuilder builder = new GenericSqlBuilder();

    @Test
    void buildSelect() {
        final SelectExpression select = new SelectExpression(
            List.of(
                new ColumnExpression(new ColumnLiteralExpression("p", "plan_id"), "plan_id"),
                new ColumnExpression(new ColumnLiteralExpression("p", "name"), "name")
            ),
            new FromExpression(new TableLiteralExpression("pls_plan"), "p", List.of()),
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

        assertEquals("SELECT \"p\".\"plan_id\", \"p\".\"name\" FROM \"pls_plan\" AS p", sql.toString());
    }
}