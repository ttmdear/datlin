package ${execution.packageName};

import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;
import ${execution.record.canonicalName};

public class ${execution.simpleName} {

    @Nonnull
    private final ${execution.resultSetProcessor} resultSetProcessor = new ${execution.resultSetProcessor}();

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
        final SelectExecution<${execution.record.simpleName}> selectExecution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
                resultSet -> {
                    return new ${execution.record.simpleName}(
                    <#list execution.record.fields as field>
                        resultSetProcessor.getValue(resultSet, "${execution.table}", "${field.columnName}", ${execution.record.simpleName}.class, "${field.name}", ${field.type}.class)<#if field?has_next>,</#if>
                    </#list>
                    );
                }
            );

            selectExecution.from("public", "${execution.table}", "t1");

            return selectExecution;
    }
}