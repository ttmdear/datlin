package io.datlin.sql.exe;

import io.datlin.sql.ast.Assignment;
import io.datlin.sql.ast.Criteria;
import io.datlin.sql.ast.SqlFragment;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.ast.Update;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.InsertExecutionException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateExecution {

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    @Nonnull
    private Update update = Update.update();

    public UpdateExecution(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    // delegated update ------------------------------------------------------------------------------------------------

    @Nonnull
    public UpdateExecution table(@Nonnull final TableReference table) {
        update = update.table(table);
        return this;
    }

    @Nonnull
    public UpdateExecution sets(@Nonnull final List<Assignment> sets) {
        update = update.sets(sets);
        return this;
    }

    @Nonnull
    public UpdateExecution where(@Nonnull final SqlFragment where) {
        update = update.where(where);
        return this;
    }

    @Nonnull
    public UpdateExecution where(@Nonnull final SqlFragment... where) {
        update = update.where(where);
        return this;
    }

    @Nonnull
    public UpdateExecution where(@Nonnull final List<SqlFragment> where) {
        update = update.where(where);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(update, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new InsertExecutionException(sql.toString(), e);
        }
    }
}
