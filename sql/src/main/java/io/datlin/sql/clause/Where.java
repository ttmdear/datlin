package io.datlin.sql.clause;

import io.datlin.sql.sql.*;

import java.util.ArrayList;
import java.util.List;

public final class Where implements Expression {

    final LogicOperator operator;
    final List<Condition> conditions = new ArrayList<>();

    Where(LogicOperator operator) {
        this.operator = operator;
    }

    public Where equal(String identifier, Object value) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.EQUAL, new ValueExpression(value)));
        return this;
    }

    public Where in(String identifier, List<?> values) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.IN, new ValuesExpression(values)));
        return this;
    }

    public Where gt(String identifier, Object value) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.GT, new ValueExpression(value)));
        return this;
    }

    public Where gte(String identifier, Object value) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.GTE, new ValueExpression(value)));
        return this;
    }

    public Where lt(String identifier, Object value) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.LT, new ValueExpression(value)));
        return this;
    }

    public Where lte(String identifier, Object value) {
        conditions.add(new Condition(Identifier.of(identifier), RelationOperator.LTE, new ValueExpression(value)));
        return this;
    }

    record Condition(
        Expression left,
        RelationOperator operator,
        Expression right
    ) {
    }

    public interface WhereConfigurer {
        void configure(Where where);
    }
}
