package io.datlin.sql.exe;

import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.InsertBuilder;
import io.datlin.sql.bld.LogicalBuilder;
import io.datlin.sql.bld.LogicalBuilder.LogicalConfigurer;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.bld.UpdateBuilder;
import io.datlin.sql.exc.InsertExecutionException;
import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateExecution {

    @Nonnull
    private final UpdateBuilder builder = new UpdateBuilder();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    public UpdateExecution(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    // delegated builder -----------------------------------------------------------------------------------------------

    @Nonnull
    public UpdateExecution table(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        builder.table(schema, name);
        return this;
    }

    @Nonnull
    public UpdateExecution table(@Nonnull final String name) {
        builder.table(name);
        return this;
    }

    @Nonnull
    public UpdateExecution where(@Nonnull final LogicalConfigurer configurer) {
        builder.where(configurer);
        return this;
    }

    @Nonnull
    public UpdateExecution set(
        @Nonnull final String column,
        @Nonnull final Object value
    ) {
        builder.set(column, value);
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
