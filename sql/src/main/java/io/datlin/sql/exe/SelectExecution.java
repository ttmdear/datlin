package io.datlin.sql.exe;

import io.datlin.sql.ast.FunctionCall;
import io.datlin.sql.ast.Select;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.DatlinSqlExecuteException;
import io.datlin.sql.logger.DatlinLogger;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class SelectExecution<T> {

    @Nonnull
    private Select select = Select.select();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder builder;

    @Nonnull
    private final Function<ResultSet, T> resultMapper;

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
    public List<T> fetchAll() {
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
            throw DatlinLogger.logAndReturn(
                new DatlinSqlExecuteException("Error during fetching all records", sql.toString(), e),
                log,
                sql.toString()
            );
        }
    }

    @Nonnull
    public T fetchOne() {
        final List<T> result = fetchAll();

        if (result.isEmpty()) {
            throw new NoSuchElementException("No one element found.");
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new IllegalStateException("More than one element found.");
        }
    }

    @Nonnull
    public Optional<T> find() {
        final List<T> result = fetchAll();

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Nonnull
    public Optional<T> findOne() {
        final List<T> result = fetchAll();

        if (result.isEmpty()) {
            return Optional.empty();
        } else if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else {
            throw new IllegalStateException("More than one element found.");
        }
    }

    @Nonnull
    public Stream<T> stream() {
        return fetchAll().stream();
    }

    /**
     * Returns the total number of records matching the criteria.
     *
     * @return the count of records.
     */
    public long count() {
        final Select selectCount = Select.select()
            .columns(
                FunctionCall.countAll()
            ).from(select.as("count"));

        final List<T> result = new ArrayList<>();
        final BuildContext buildContext = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        builder.build(selectCount, sql, buildContext);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            buildContext.prepareStatement(statement);

            try (final ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw DatlinLogger.logAndReturn(
                new DatlinSqlExecuteException("Error during counting records", sql.toString(), e),
                log,
                sql.toString()
            );
        }

        return 0;
    }

    /**
     * Checks if any record exists that matches the criteria.
     *
     * @return true if at least one record exists, false otherwise.
     */
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fetches results and maps them into a Map using the provided key mapper.
     */
    public <K> Map<K, T> fetchMap(@Nonnull Function<? super T, ? extends K> keyMapper) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fetches a single value (e.g., from SELECT MAX(age)).
     *
     * @return an Optional containing the scalar value.
     */
    public <S> Optional<S> fetchScalar(@Nonnull Class<S> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * Executes the query and maps the results directly to a DTO class.
     */
    public <R> List<R> fetchAs(@Nonnull Class<R> dtoClass) {
        throw new UnsupportedOperationException();
    }

    public <U> U mapOne(Function<? super T, ? extends U> mapper) {
        return mapper.apply(fetchOne());
    }

    public <U> List<U> mapAll(Function<? super T, ? extends U> mapper) {
        return fetchAll().stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}