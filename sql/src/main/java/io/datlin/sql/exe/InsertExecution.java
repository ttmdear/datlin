package io.datlin.sql.exe;

import io.datlin.sql.ast.Insert;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.bld.BuildContext;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exc.DatlinSqlExecuteException;
import io.datlin.sql.logger.DatlinLogger;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class InsertExecution {

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    @Nonnull
    private Insert insert = Insert.insert();

    // delegated from insert -------------------------------------------------------------------------------------------

    @Nonnull
    public InsertExecution into(@Nonnull final TableReference table) {
        insert = insert.into(table);
        return this;
    }

    @Nonnull
    public InsertExecution columns(@Nonnull final List<String> columns) {
        insert = insert.columns(columns);
        return this;
    }

    @Nonnull
    public InsertExecution values(@Nonnull final List<List<Object>> values) {
        insert = insert.values(values);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void execute() {
        final BuildContext context = new BuildContext();
        final StringBuilder sql = new StringBuilder();
        sqlBuilder.build(insert, sql, context);

        try (final PreparedStatement statement = executionConnection.getConnection().prepareStatement(sql.toString())) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw DatlinLogger.logAndReturn(
                new DatlinSqlExecuteException("Error during executing INSERT", sql.toString(), e),
                log,
                sql.toString()
            );
        }
    }
}
