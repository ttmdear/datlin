package io.datlin.sql.exe;

import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.InsertExecutionException;
import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InsertExecution {
//
////    @Nonnull
////    private final InsertBuilder builder = new InsertBuilder();
//
//    @Nonnull
//    private final ExecutionConnection executionConnection;
//
//    @Nonnull
//    private final SqlBuilder sqlBuilder;
//
//    public InsertExecution(
//        @Nonnull final ExecutionConnection executionConnection,
//        @Nonnull final SqlBuilder sqlBuilder
//    ) {
//        this.executionConnection = executionConnection;
//        this.sqlBuilder = sqlBuilder;
//    }
//
//    // delegated InsertBuilder -----------------------------------------------------------------------------------------
//
//    @Nonnull
//    public InsertExecution into(@Nonnull final String name) {
//        builder.into(name);
//        return this;
//    }
//
//    @Nonnull
//    public InsertExecution into(
//        @Nonnull final String schema,
//        @Nonnull final String name
//    ) {
//        builder.into(schema, name);
//        return this;
//    }
//
//    @Nonnull
//    public InsertExecution fields(@Nonnull final List<String> fields) {
//        builder.fields(fields);
//        return this;
//    }
//
//    @Nonnull
//    public InsertExecution fields(@Nonnull final String... fields) {
//        builder.fields(fields);
//        return this;
//    }
//
//    @Nonnull
//    public InsertExecution values(@Nonnull final List<Object> values) {
//        builder.values(values);
//        return this;
//    }
//
//    @Nonnull
//    public InsertExecution values(@Nonnull final Object... values) {
//        builder.values(values);
//        return this;
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//
//    public void execute() {
//        final BuildContext context = new BuildContext();
//        final StringBuilder sql = new StringBuilder();
//        sqlBuilder.build(builder.build(), sql, context);
//
//        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
//            context.prepareStatement(statement);
//            statement.execute();
//        } catch (SQLException e) {
//            throw new InsertExecutionException(sql.toString(), e);
//        }
//    }
}
