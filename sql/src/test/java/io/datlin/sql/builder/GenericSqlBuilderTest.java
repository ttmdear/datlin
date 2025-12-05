package io.datlin.sql.builder;

import io.datlin.sql.expression.ConditionsExpression;
import io.datlin.sql.expression.FromExpression;
import io.datlin.sql.expression.SelectExpression;
import io.datlin.sql.expression.TableLiteralExpression;
import io.datlin.sql.sql.BuildContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.datlin.sql.expression.LogicOperator.AND;

class GenericSqlBuilderTest {
    GenericSqlBuilder builder = new GenericSqlBuilder();

    @Test
    void buildSelect() {
        final SelectExpression select = new SelectExpression(
            List.of(),
            new FromExpression(new TableLiteralExpression("pls_plan"), "p", List.of()),
            new ConditionsExpression(AND, List.of(

            ))
        );

        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

        System.out.printf("sql: %s\n", sql);
    }
}