package io.datlin.sql.exe;

import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.ast.TableReference;
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
public class DeleteExecution {

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    @Nonnull
    private Delete delete = Delete.delete();

    // delegated delete ------------------------------------------------------------------------------------------------

    @Nonnull
    public DeleteExecution from(@Nonnull final TableReference table) {
        delete = delete.from(table);
        return this;
    }

    @Nonnull
    public DeleteExecution where(@Nonnull final SqlFragment where) {
        delete = delete.where(where);
        return this;
    }

    @Nonnull
    public DeleteExecution where(@Nonnull final SqlFragment... where) {
        delete = delete.where(where);
        return this;
    }

    @Nonnull
    public DeleteExecution where(@Nonnull final List<SqlFragment> where) {
        delete = delete.where(where);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(delete, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw logAndReturn(
                new DatlinSqlExecuteException("Error during executing DELETE", sql.toString(), e), log, sql.toString()
            );
        }
    }
}
