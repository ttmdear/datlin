package io.datlin.sql.exe;

import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.LogicalOperator;
import io.datlin.sql.ast.PostgresUpsert;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.DatlinSqlExecuteException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static io.datlin.sql.logger.DatlinLogger.logAndReturn;

@Slf4j
@RequiredArgsConstructor
public class ListUpdateExecution<T> {

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    @Nonnull
    private final List<T> records;

    @Nullable
    private Criteria deleteOrphanWhere = null;

    @Nonnull
    private final OrphanDeleteFactory<T> orphanDeleteFactory;

    @Nonnull
    private final PostgresUpsertFactory<T> postgresUpsertFactory;

    @Nonnull
    public ListUpdateExecution<T> deleteOrphan(@Nonnull final SqlFragment where) {
        this.deleteOrphanWhere = new Criteria(
            LogicalOperator.AND,
            List.of(where),
            null
        );

        return this;
    }

    @Nonnull
    public ListUpdateExecution<T> deleteOrphan(@Nonnull final SqlFragment... where) {
        this.deleteOrphanWhere = new Criteria(
            LogicalOperator.AND,
            List.of(where),
            null
        );

        return this;
    }

    @Nonnull
    public ListUpdateExecution<T> deleteOrphan(@Nonnull final List<SqlFragment> where) {
        this.deleteOrphanWhere = new Criteria(
            LogicalOperator.AND,
            List.copyOf(where),
            null
        );

        return this;
    }

    public void execute() {
        executeDeleteOrphan();

        final PostgresUpsert upsert = postgresUpsertFactory.create();
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        sqlBuilder.build(upsert, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            for (final T record : records) {
                postgresUpsertFactory.setStatementObjects(statement, record);
                statement.addBatch();
            }

            statement.execute();
        } catch (SQLException e) {
            throw logAndReturn(
                new DatlinSqlExecuteException("Error during executing UPDATE", sql.toString(), e),
                log,
                sql.toString()
            );
        }
    }

    private void executeDeleteOrphan() {
        if (deleteOrphanWhere == null) {
            return;
        }

        final Delete delete = orphanDeleteFactory.create(deleteOrphanWhere, records);
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();

        sqlBuilder.build(delete, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw logAndReturn(
                new DatlinSqlExecuteException("Error during executing DELETE", sql.toString(), e),
                log,
                sql.toString()
            );
        }
    }

    public interface PostgresUpsertFactory<T> {

        @Nonnull
        default PostgresUpsert create() {
            throw new RuntimeException("Not implemented");
        }

        default void setStatementObjects(
            @Nonnull final PreparedStatement statement,
            @Nonnull final T record
        ) throws SQLException {
            throw new RuntimeException("Not implemented");
        }
    }

    public interface OrphanDeleteFactory<T> {

        @Nonnull
        default Delete create(
            @Nonnull final Criteria deleteOrphanWhere,
            @Nonnull final List<T> records
        ) {
            throw new RuntimeException("Not implemented");
        }
    }
}
