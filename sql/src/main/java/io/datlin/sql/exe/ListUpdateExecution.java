package io.datlin.sql.exe;

import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.LogicalOperator;
import io.datlin.sql.ast.Update;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.DatlinSqlExecuteException;
import jakarta.annotation.Nonnull;
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

    @Nonnull
    private final Preprocess<T> preprocess;

    @Nonnull
    private final Criteria where = new Criteria(LogicalOperator.AND, List.of(), null);

    public void execute() {
        // delete.where(
        //     not(where())
        // ).execute();

        final Update update = preprocess.createUpdate();
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(update, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            for (final T record : records) {
                preprocess.setStatementObjects(statement, record);
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

    public interface Preprocess<T> {

        @Nonnull
        Update createUpdate();

        @Nonnull
        Delete createDelete();

        void setStatementObjects(
            @Nonnull final PreparedStatement statement,
            @Nonnull final T record
        ) throws SQLException;
    }
}
