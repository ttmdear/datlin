package io.datlin.sql.builder;

import io.datlin.sql.clause.From;
import io.datlin.sql.clause.From.TableSource;
import io.datlin.sql.query.Select;
import io.datlin.sql.sql.BuildContext;
import org.junit.jupiter.api.Test;

class PostgreSqlBuilderTest {
    PostgreSqlBuilder postgreSqlBuilder = new PostgreSqlBuilder();

    @Test
    void buildSelect() {
        final Select select = new Select(new From(new TableSource("pls_plan"), "p"));


        final BuildContext buildContext = new BuildContext();

        postgreSqlBuilder.build(select, buildContext);

        System.out.printf("test");
    }
}