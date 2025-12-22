package io.datlin.sql.bld;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericSqlBuilderTest {
//
//    @Nonnull
//    private final GenericSqlBuilder builder = new GenericSqlBuilder();
//
//    @Test
//    void buildSelect() {
////        final Select select = new Select(
////            List.of(
////                new SelectColumnReference(new ColumnReference("p", "plan_id"), "plan_id"),
////                new SelectColumnReference(new ColumnReference("p", "name"), "name")
////            ),
////            new FromNode(new TableReference("public", "pls_plan"), "p", List.of()),
////            new LogicalNode(AND, List.of(
////                new ComparisonNode(
////                    new ColumnReference("p", "plan_id"),
////                    ComparisonOperator.EQ,
////                    new ValueNode(UUID.fromString("b2c63d7c-d2b2-11f0-8de9-0242ac120002"))
////                )
////            ))
////        );
////
////        final BuildContext buildContext = new BuildContext();
////        final StringBuilder sql = new StringBuilder();
////
////        builder.build(select, sql, buildContext);
////
////        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p " +
////            "WHERE (\"p\".\"plan_id\" = ?)", sql.toString());
////        assertEquals(1, buildContext.getStatementObjects().size());
////        assertEquals(UUID.fromString("b2c63d7c-d2b2-11f0-8de9-0242ac120002"), buildContext.getStatementObject(0));
//    }
//
//    @Test
//    void buildSelectNoWhere() {
//        final Select select = new Select(
//            List.of(
//                new SelectColumnReference(new ColumnReference("p", "plan_id"), "plan_id"),
//                new SelectColumnReference(new ColumnReference("p", "name"), "name")
//            ),
//            new FromNode(new TableReference("public", "pls_plan"), "p", List.of()),
//            null
//        );
//
//        final BuildContext buildContext = new BuildContext();
//        final StringBuilder sql = new StringBuilder();
//
//        builder.build(select, sql, buildContext);
//
//        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p",
//            sql.toString());
//    }
//
//    @Test
//    void buildSelectEmptyWhere() {
//        final Select select = new Select(
//            List.of(
//                new SelectColumnReference(new ColumnReference("p", "plan_id"), "plan_id"),
//                new SelectColumnReference(new ColumnReference("p", "name"), "name")
//            ),
//            new FromNode(new TableReference("public", "pls_plan"), "p", List.of()),
//            new LogicalNode(AND, List.of())
//        );
//
//        final BuildContext buildContext = new BuildContext();
//        final StringBuilder sql = new StringBuilder();
//
//        builder.build(select, sql, buildContext);
//
//        assertEquals("SELECT \"p\".\"plan_id\" AS plan_id, \"p\".\"name\" AS name FROM \"public\".\"pls_plan\" AS p",
//            sql.toString());
//    }
}