package io.datlin.sql.query;

import io.datlin.sql.clause.Where;
import io.datlin.sql.expression.Expression;
import io.datlin.sql.sql.Identifier;
import jakarta.annotation.Nullable;

public final class Delete implements Expression {

    @Nullable
    Identifier table;

    @Nullable
    Where where;

    Delete() {

    }

    public static Delete build() {
        return new Delete();
    }

    // value ------------------------------------------------------------------------------------------------------------
    public Delete table(String table) {
        // this.value = Identifier.of(value);
        return this;
    }

    // where -----------------------------------------------------------------------------------------------------------
    public Where where() {
        // this.where = new Where(LogicOperator.AND);
        return this.where;
    }

    public Delete where(Where.WhereConfigurer configurer) {
        // if (where == null) {
        //     where = new Where(LogicOperator.AND);
        // }

        // configurer.configure(where);

        return this;
    }
}