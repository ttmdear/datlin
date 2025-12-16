package io.datlin.sql.exe;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.DeleteBuilder;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.bld.UpdateBuilder;
import io.datlin.sql.exc.InsertExecutionException;

public class DeleteExecution {

    @Nonnull
    private final DeleteBuilder builder = new DeleteBuilder();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    public DeleteExecution(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    // delegated builder -----------------------------------------------------------------------------------------------

    @Nonnull
    public DeleteExecution table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        builder.table(schema, name);
        return this;
    }

    @Nonnull
    public DeleteExecution table(@Nonnull final String name) {
        builder.table(name);
        return this;
    }

    @Nonnull
    public DeleteExecution where(@Nonnull final LogicalConfigurer configurer) {
        builder.where(configurer);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(builder.build(), sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new InsertExecutionException(sql.toString(), e);
        }
    }
}
