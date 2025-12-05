package io.datlin.sql.sql;

import io.datlin.sql.expression.Expression;

public class RelationExpression implements Expression
{

    private final Expression left;
    private final RelationOperator operator;
    private final RelationOperator right;

    public RelationExpression(Expression left, RelationOperator operator, RelationOperator right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
