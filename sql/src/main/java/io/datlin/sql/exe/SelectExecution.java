package io.datlin.sql.exe;

import io.datlin.sql.bld.SqlBuilder;
import jakarta.annotation.Nonnull;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

public class SelectExecution<T> {
//
//    // @Nonnull
//    // private final Select select = new Select();
//
//    @Nonnull
//    private final ExecutionConnection executionConnection;
//
//    @Nonnull
//    private final SqlBuilder sqlBuilder;
//
//    @Nonnull
//    private final Function<ResultSet, T> resultMapper;
//
//    public SelectExecution(
//        @Nonnull final ExecutionConnection executionConnection,
//        @Nonnull final SqlBuilder sqlBuilder,
//        @Nonnull final Function<ResultSet, T> resultMapper
//    ) {
//        this.executionConnection = executionConnection;
//        this.sqlBuilder = sqlBuilder;
//        this.resultMapper = resultMapper;
//    }
//
//    @Nonnull
//    public SelectExecution<T> column(
//        @Nonnull String table,
//        @Nonnull String column,
//        @Nonnull String alias
//    ) {
////        builder.column(table, column, alias);
////        return this;
//        return this;
//    }
//
//    @Nonnull
//    public SelectExecution<T> from(
//        @Nonnull final String schema,
//        @Nonnull final String name,
//        @Nonnull final String alias
//    ) {
////        builder.from(schema, name, alias);
////        return this;
//        return this;
//    }
//
//    @Nonnull
//    public SelectExecution<T> where(
//        @Nonnull final LogicalBuilder.LogicalConfigurer configurer
//    ) {
//        // builder.where(configurer);
//        return this;
//    }
//
//    @Nonnull
//    public List<T> fetch() {
////        final List<T> result = new ArrayList<>();
////        final Select select = builder.build();
////        final BuildContext buildContext = new BuildContext();
////        final StringBuilder sql = new StringBuilder();
////
////        sqlBuilder.build(select, sql, buildContext);
////
////        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
////            buildContext.prepareStatement(statement);
////
////            final ResultSet resultSet = statement.executeQuery();
////
////            while (resultSet.next()) {
////                result.add(resultMapper.apply(resultSet));
////            }
////
////            return result;
////        } catch (SQLException e) {
////            throw new FetchSQLException(sql.toString(), e);
////        }
//
//        return List.of();
//    }
}
