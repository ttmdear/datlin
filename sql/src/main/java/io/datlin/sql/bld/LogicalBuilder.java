package io.datlin.sql.bld;

import io.datlin.sql.ast.*;
import jakarta.annotation.Nonnull;

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

    @Nonnull
    public LogicalBuilder eq(
        @Nonnull final String table,
        @Nonnull final String column,
        @Nonnull final UUID value
    ) {
        criteria.add(
            new ComparisonNode(
                new ColumnLiteralNode(table, column),
                ComparisonOperator.EQ,
                new UuidValueNode(value)
            )
        );

        return this;
    }

    @Nonnull
    public LogicalNode build() {
        final List<Node> conditions = new ArrayList<>(this.criteria.size());
        for (final Object condition : this.criteria) {
            if (condition instanceof LogicalBuilder logicalBuilder) {
                conditions.add(logicalBuilder.build());
            } else if (condition instanceof Node node) {
                conditions.add(node);
            }
        }
        return new LogicalNode(operator, conditions);
    }

    public interface LogicalConfigurer {
        void configure(final LogicalBuilder builder);
    }
}