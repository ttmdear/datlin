package io.datlin.sql.bld;

import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.TableReference;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.TableReference.table;

class SelectBuilderTest {

    @Nonnull
    private final GenericSqlBuilder sqlBuilder = new GenericSqlBuilder();

    @Test
    void build() {
        final Select select = Select.select()
            .columns(
                column("column1").from("p").as("column_2"),
                column("column2").from("p"),
                column("column3").as("column_2"),
                column("column4")
            )
            .from(
                table("plan"),
            );

        System.out.printf("test");
        // final SelectNode select = new SelectBuilder()
        //     .column("p", "plan_id", "plan_id")
        //     .from("public", "pls_plan", "p")
        //     .where(builder -> {
        //         builder.eq("p", "plan_id", UUID.fromString("9747e23a-e664-405e-9e3a-0c23e1c42c57"));
        //     })
        //     .build();

        // final StringBuilder sql = new StringBuilder();
        // final BuildContext buildContext = new BuildContext();
        // sqlBuilder.build(select, sql, buildContext);

        // assertEquals("SELECT \"p\".\"plan_id\" AS plan_id FROM \"public\".\"pls_plan\" AS p WHERE (\"p\".\"plan_id\" = ?)", sql.toString());

        // assertEquals(1, buildContext.getStatementObjects().size());
        // assertEquals(UUID.fromString("9747e23a-e664-405e-9e3a-0c23e1c42c57"), buildContext.getStatementObject(0));
    }

    // @Test
    // void buildNoWhere() {
    //     select()
    //         .columns(
    //             column("...").as(...),
    //             column("").from("")
    //         )
    //         .from("pu")
    //         .from(from -> {
    //             from.as("..."),
    //             from.as("..."),
    //         })
    //         .column("")
    //         .column("", c -> c.alias().from())
    //         .where(where -> {
    //             ...
    //         })

    //     final SelectNode select = new SelectBuilder()
    //         .columns(column("...").as(""))
    //         .unsetClumns()
    //         .columns(
    //             column("...").as();
    //             column("...").from().as().min().build()
    //             column("...").as();
    //             column("...").as();
    //             ...,
    //             ...,
    //             ...,
    //             ...,
    //             ...,
    //         )
    //         .column(column().as("..."))
    //         .column(column().as("..."))
    //         .column(column().as("..."))
    //         .column(column().as("..."))

    //         .column("p", "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column(UserPlnTable.name, "plan_id", "plan_id")
    //         .column("plan_id", "plan_id")
    //         .column("p", "plan_id")
    //         .column("plan_id")

    //         .from("pls_plan")
    //         .from("public", "pls_plan")
    //         .from("public", "pls_plan", "as")
    //         .from(from("...").as(....))
    //         .from("public", "pls_plan", "p")
    //         .build();

    //     final StringBuilder sql = new StringBuilder();
    //     final BuildContext buildContext = new BuildContext();
    //     sqlBuilder.build(select, sql, buildContext);

    //     assertEquals("SELECT \"p\".\"plan_id\" AS plan_id FROM \"public\".\"pls_plan\" AS p", sql.toString());
    //     assertEquals(0, buildContext.getStatementObjects().size());
    // }

    // @Test
    // void buildNoAliases() {
    //     final SelectNode select = new SelectBuilder()
    //         .column("plan_id", "plan_id")
    //         .from("public", "pls_plan", "p")
    //         .build();

    //     final StringBuilder sql = new StringBuilder();
    //     final BuildContext buildContext = new BuildContext();
    //     sqlBuilder.build(select, sql, buildContext);

    //     assertEquals("SELECT \"p\".\"plan_id\" AS plan_id FROM \"public\".\"pls_plan\" AS p", sql.toString());
    //     assertEquals(0, buildContext.getStatementObjects().size());
    // }
}