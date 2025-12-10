package ${execution.packageName};

import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.sql.ResultSetMapper;
import jakarta.annotation.Nonnull;
import ${execution.record.canonicalName};

import java.sql.SQLException;

public class ${execution.simpleName} {

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    public ${execution.simpleName}(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    public SelectExecution<${execution.record.simpleName}> select() {
        final ResultSetMapper resultSetMapper = new ResultSetMapper();

        final SelectExecution<${execution.record.simpleName}> selectExecution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
                resultSet -> {
                    try {
                        return new ${execution.record.simpleName}(
                        <#list execution.record.fields as field>
                            resultSetMapper.map(resultSet.getObject("${field.columnName}"), ${field.type}.class)<#if field?has_next>,</#if>
                        </#list>
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            );

            selectExecution.from("public", "${execution.table}", "t1");

            return selectExecution;
    }
}