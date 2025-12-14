package ${execution.packageName};

import java.util.ArrayList;
import java.util.List;

import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.InsertExecution;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.exe.UpdateExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;
import ${execution.record.canonicalName};

public class ${execution.simpleName} {

    @Nonnull
    private static final List<String> INSERT_FIELDS;

    @Nonnull
    private final ${execution.resultSetProcessor} resultSetProcessor = new ${execution.resultSetProcessor}();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    static {
        INSERT_FIELDS = new ArrayList<>();
        <#list execution.record.fields as field>
        INSERT_FIELDS.add("${field.columnName}");
        </#list>
    }

    public ${execution.simpleName}(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    @Nonnull
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

    @Nonnull
    public InsertExecution insert() {
        return new InsertExecution(executionConnection, sqlBuilder)
            .into("public", "${execution.table}");
    }

    @Nonnull
    public InsertExecution insert(@Nonnull final ${execution.record.simpleName} record) {
        final InsertExecution insert = new InsertExecution(executionConnection, sqlBuilder)
            .into("public", "${execution.table}")
            .fields(INSERT_FIELDS);

        final List<Object> values = new ArrayList<>();
        <#list execution.record.fields as field>
        values.add(record.${field.name}());
        </#list>
        insert.values(values);

        return insert;
    }

    @Nonnull
    public UpdateExecution update() {
        return new UpdateExecution(executionConnection, sqlBuilder)
            .table("public", "${execution.table}");
    }

    @Nonnull
    public UpdateExecution update(@Nonnull final ${execution.record.simpleName} record) {
        final UpdateExecution update = new UpdateExecution(executionConnection, sqlBuilder)
            .table("public", "${execution.table}");

        <#list execution.record.fields as field>
        update.set("${field.columnName}", record.${field.name}());
        </#list>
        update.where(where -> {
            <#list execution.record.primaryKeys as field>
            where.eq("${execution.table}", "${field.columnName}", record.${field.name}());
            </#list>
        });

        return update;
    }
}