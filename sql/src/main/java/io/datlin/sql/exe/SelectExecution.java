package io.datlin.sql.exe;

import io.datlin.sql.bld.SelectBuilder;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.FetchSQLException;
import io.datlin.sql.bld.LogicalBuilder;
import io.datlin.sql.ast.SelectNode;
import io.datlin.sql.sql.BuildContext;
import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SelectExecution<T> {

    @Nonnull
    private final SelectBuilder builder = new SelectBuilder();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    @Nonnull
    private final Function<ResultSet, T> resultMapper;

    public SelectExecution(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder,
        @Nonnull final Function<ResultSet, T> resultMapper
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
        this.resultMapper = resultMapper;
    }

    @Nonnull
    public SelectExecution<T> column(
        @Nonnull String table,
        @Nonnull String column,
        @Nonnull String alias
    ) {
        builder.column(table, column, alias);
        return this;
    }

    @Nonnull
    public SelectExecution<T> from(
        final @Nonnull String schema,
        final @Nonnull String name,
        final @Nonnull String alias
    ) {
        builder.from(schema, name, alias);
        return this;
    }

    @Nonnull
    public SelectExecution<T> where(
        final @Nonnull LogicalBuilder.LogicalConfigurer configurer
    ) {
        builder.where(configurer);
        return this;
    }

    @Nonnull
    public List<T> fetch() {
        final List<T> result = new ArrayList<>();
        final SelectNode select = builder.build();
        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        sqlBuilder.build(select, sql, buildContext);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            buildContext.prepareStatement(statement);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(resultMapper.apply(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new FetchSQLException(sql.toString(), e);
        }
    }
}
