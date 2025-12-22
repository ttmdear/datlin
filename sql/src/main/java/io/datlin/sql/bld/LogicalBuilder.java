package io.datlin.sql.bld;

import io.datlin.sql.ast.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogicalBuilder {

    @Nonnull
    private LogicalOperator operator = LogicalOperator.AND;

    @Nonnull
    private final List<Object> criteria = new ArrayList<>();

    public LogicalBuilder() {

    }

    public LogicalBuilder(@Nonnull final LogicalOperator operator) {
        this.operator = operator;
    }

    @Nonnull
    public LogicalBuilder or(@Nonnull final LogicalConfigurer configurer) {
        final LogicalBuilder builder = new LogicalBuilder(LogicalOperator.OR);
        configurer.configure(builder);
        return builder;
    }

    @Nonnull
    public LogicalBuilder and(@Nonnull final LogicalConfigurer configurer) {
        final LogicalBuilder builder = new LogicalBuilder(LogicalOperator.AND);
        configurer.configure(builder);
        return builder;
    }

    // eq --------------------------------------------------------------------------------------------------------------

    @Nonnull
    public LogicalBuilder eq(
        @Nonnull final String column,
        @Nonnull final UUID value
    ) {
//        criteria.add(
//            new ComparisonNode(
//                new ColumnReference(null, column),
//                ComparisonOperator.EQ,
//                new ValueNode(value)
//            )
//        );
//
//        return this;

        return this;
    }

    @Nonnull
    public LogicalBuilder eq(
        @Nullable final String table,
        @Nonnull final String column,
        @Nonnull final UUID value
    ) {
//        criteria.add(
//            new ComparisonNode(
//                new ColumnReference(table, column),
//                ComparisonOperator.EQ,
//                new ValueNode(value)
//            )
//        );
//
//        return this;

        return this;
    }

    // build -----------------------------------------------------------------------------------------------------------

    @Nonnull
    public LogicalNode build() {
        final List<SqlFragment> conditions = new ArrayList<>(this.criteria.size());
        for (final Object condition : this.criteria) {
            if (condition instanceof LogicalBuilder logicalBuilder) {
                conditions.add(logicalBuilder.build());
            } else if (condition instanceof SqlFragment sqlFragment) {
                conditions.add(sqlFragment);
            }
        }
        return new LogicalNode(operator, conditions);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public interface LogicalConfigurer {
        void configure(final LogicalBuilder builder);
    }
}