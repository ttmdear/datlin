<#-- @ftlvariable name="executionCodeModel" type="io.datlin.rcm.ExecutionCodeModel" -->
package ${executionCodeModel.packageName};

import java.util.ArrayList;
import java.util.List;

import io.datlin.sql.ast.Assignment;
import io.datlin.sql.ast.TableReference;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.DeleteExecution;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.InsertExecution;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.exe.UpdateExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;

import ${executionCodeModel.tableCodeModel.canonicalName};
import ${executionCodeModel.recordCodeModel.canonicalName};

import static io.datlin.sql.ast.Assignment.set;

public class ${executionCodeModel.simpleName} {

    @Nonnull
    private static final List<String> INSERT_FIELDS;

    @Nonnull
    private final ${executionCodeModel.resultSetProcessor} resultSetProcessor = new ${executionCodeModel.resultSetProcessor}();

    @Nonnull
    private final ExecutionConnection executionConnection;

    @Nonnull
    private final SqlBuilder sqlBuilder;

    static {
        INSERT_FIELDS = new ArrayList<>();
        <#list executionCodeModel.recordCodeModel.fields as field>
        INSERT_FIELDS.add("${field.columnMetadata.name}");
        </#list>
    }

    public ${executionCodeModel.simpleName}(
        @Nonnull final ExecutionConnection executionConnection,
        @Nonnull final SqlBuilder sqlBuilder
    ) {
        this.executionConnection = executionConnection;
        this.sqlBuilder = sqlBuilder;
    }

    @Nonnull
    public SelectExecution<${executionCodeModel.recordCodeModel.simpleName}> select() {
        final SelectExecution<${executionCodeModel.recordCodeModel.simpleName}> execution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
            resultSet -> {
                return new ${executionCodeModel.recordCodeModel.simpleName}(
                <#list executionCodeModel.recordCodeModel.fields as field>
                    resultSetProcessor.getValue(resultSet, "${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnMetadata.name}", ${executionCodeModel.recordCodeModel.simpleName}.class, "${field.name}", ${field.type}.class)<#if field?has_next>,</#if>
                </#list>
                );
            }
        );

        execution.from(TableReference.table("${executionCodeModel.tableCodeModel.tableMetadata.name}");

        return execution;
    }

    @Nonnull
    public InsertExecution insert() {
        return new InsertExecution(executionConnection, sqlBuilder)
            .into(${executionCodeModel.tableCodeModel.simpleName}.${executionCodeModel.tableCodeModel.field});
    }

    @Nonnull
    public InsertExecution insert(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final InsertExecution insert = new InsertExecution(executionConnection, sqlBuilder)
            .into(${executionCodeModel.tableCodeModel.simpleName}.${executionCodeModel.tableCodeModel.field})
            .columns(INSERT_FIELDS);

        final List<Object> values = new ArrayList<>();
        <#list executionCodeModel.recordCodeModel.fields as field>
        values.add(record.${field.name}());
        </#list>
        insert.values(List.of(values));

        return insert;
    }

    @Nonnull
    public UpdateExecution update() {
        return new UpdateExecution(executionConnection, sqlBuilder)
            .table(${executionCodeModel.tableCodeModel.simpleName}.${executionCodeModel.tableCodeModel.field});
    }

    @Nonnull
    public UpdateExecution update(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final UpdateExecution update = new UpdateExecution(executionConnection, sqlBuilder)
            .table(TableReference.table("${executionCodeModel.tableCodeModel.tableMetadata.name}");

        final List<Assignment> assignments = new ArrayList<>();
        <#list executionCodeModel.recordCodeModel.fields as field>
        assignments.add(set(${field.}.${name}, record.planId()));
        </#list>
        update.sets(assignments);

        update.where(where -> {
            <#list executionCodeModel.recordCodeModel.primaryKeys as field>
            where.eq("${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnMetadata.name}", record.${field.name}());
            </#list>
        });

        return update;
    }

    @Nonnull
    public DeleteExecution delete() {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .table(${executionCodeModel.tableCodeModel.simpleName}.${executionCodeModel.tableCodeModel.field});
    }

    @Nonnull
    public DeleteExecution delete(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final DeleteExecution delete = new DeleteExecution(executionConnection, sqlBuilder)
            .table(${executionCodeModel.tableCodeModel.simpleName}.${executionCodeModel.tableCodeModel.field});

        delete.where(where -> {
        <#list executionCodeModel.recordCodeModel.primaryKeys as field>
            where.eq("${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnMetadata.name}", record.${field.name}());
        </#list>
        });

        return delete;
    }
}