<#-- @ftlvariable name="execution" type="io.datlin.rcm.ExecutionCodeModel" -->
<#assign tableReference = execution.table.simpleName + "." + execution.table.tableReferenceField>
package ${execution.packageName};

import java.util.ArrayList;
import java.util.List;

import io.datlin.sql.ast.Assignment;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.DeleteExecution;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.InsertExecution;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.exe.UpdateExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;

import ${execution.table.canonicalName};
import ${execution.record.canonicalName};

import static io.datlin.sql.ast.Assignment.set;
import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.Comparison.eq;

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
        INSERT_FIELDS.add("${field.metadata.name}");
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
        final SelectExecution<${execution.record.simpleName}> execution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
            resultSet -> {
                return new ${execution.record.simpleName}(
                <#list execution.record.fields as field>
                    resultSetProcessor.getValue(resultSet, "${execution.table.metadata.name}", "${field.metadata.name}", ${execution.record.simpleName}.class, "${field.name}", ${field.type}.class)<#if field?has_next>,</#if>
                </#list>
                );
            }
        );

        execution.from(${tableReference});

        return execution;
    }

    @Nonnull
    public InsertExecution insert() {
        return new InsertExecution(executionConnection, sqlBuilder)
            .into(${tableReference});
    }

    @Nonnull
    public InsertExecution insert(@Nonnull final ${execution.record.simpleName} record) {
        final InsertExecution insert = new InsertExecution(executionConnection, sqlBuilder)
            .into(${tableReference})
            .columns(INSERT_FIELDS);

        final List<Object> values = new ArrayList<>();
        <#list execution.record.fields as field>
        values.add(record.${field.name}());
        </#list>
        insert.values(List.of(values));

        return insert;
    }

    @Nonnull
    public UpdateExecution update() {
        return new UpdateExecution(executionConnection, sqlBuilder)
            .table(${tableReference});
    }

    @Nonnull
    public UpdateExecution update(@Nonnull final ${execution.record.simpleName} record) {
        final UpdateExecution update = new UpdateExecution(executionConnection, sqlBuilder)
            .table(${tableReference});

        final List<Assignment> assignments = new ArrayList<>();
        <#list execution.record.fields as field>
        assignments.add(set(column("${field.metadata.name}"), record.${field.name}()));
        </#list>
        update.sets(assignments);

        update.where(
            <#list execution.record.primaryKeys as field>
            eq(column("${field.metadata.name}"), record.${field.name}())
            </#list>
        );

        return update;
    }

    @Nonnull
    public DeleteExecution delete() {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .from(${tableReference});
    }

    @Nonnull
    public DeleteExecution delete(@Nonnull final ${execution.record.simpleName} record) {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .from(${tableReference})
            .where(
            <#list execution.record.primaryKeys as field>
                eq("${field.metadata.name}", record.${field.name}())
            </#list>
            );
    }
}