package io.datlin.sql.sql;

import io.datlin.sql.ast.Node;

public class RelationExpression implements Node
{

    private final Node left;
    private final RelationOperator operator;
    private final RelationOperator right;

    public RelationExpression(Node left, RelationOperator operator, RelationOperator right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
