package io.datlin.sql.exe;

import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.FetchSQLException;
import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SelectExecution<T> {

    @Nonnull
    private Select select = Select.select();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder builder;

    @Nonnull
    private final Function<ResultSet, T> resultMapper;

    public SelectExecution(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder builder,
        @Nonnull final Function<ResultSet, T> resultMapper
    ) {
        this.executionConnection = executionConnection;
        this.builder = builder;
        this.resultMapper = resultMapper;
    }

    // select ----------------------------------------------------------------------------------------------------------

    @Nonnull
    public SelectExecution<T> columns(@Nonnull final SqlFragment... columns) {
        select = select.columns(columns);
        return this;
    }

    @Nonnull
    public SelectExecution<T> columns(@Nonnull final List<SqlFragment> columns) {
        select = select.columns(columns);
        return this;
    }

    @Nonnull
    public SelectExecution<T> from(@Nonnull final SqlFragment from) {
        select = select.from(from);
        return this;
    }

    @Nonnull
    public SelectExecution<T> joins(@Nonnull final SqlFragment... joins) {
        select = select.joins(joins);
        return this;
    }

    @Nonnull
    public SelectExecution<T> joins(@Nonnull final List<SqlFragment> joins) {
        select = select.joins(joins);
        return this;
    }

    @Nonnull
    public SelectExecution<T> where(@Nonnull final SqlFragment criteria) {
        select = select.where(criteria);
        return this;
    }

    @Nonnull
    public SelectExecution<T> where(@Nonnull final SqlFragment... criteria) {
        select = select.where(criteria);
        return this;
    }

    @Nonnull
    public SelectExecution<T> where(@Nonnull final List<SqlFragment> criteria) {
        select = select.where(criteria);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nonnull
    public List<T> fetch() {
        final List<T> result = new ArrayList<>();
        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(select, sql, buildContext);

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
