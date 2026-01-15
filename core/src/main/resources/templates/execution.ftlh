<#-- @ftlvariable name="execution" type="io.datlin.rcm.TableExecutionCodeModel" -->
package ${execution.packageName};

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.datlin.sql.ast.Assignment;
import io.datlin.sql.ast.Delete;
import io.datlin.sql.ast.RawValue;
import io.datlin.sql.ast.Update;
import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.DeleteExecution;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.InsertExecution;
import io.datlin.sql.exe.ListUpdateExecution;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.exe.UpdateExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;

import static io.datlin.sql.ast.Assignment.set;
import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.BinaryExpression.eq;

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
        <#list execution.table.record.fields as field>
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
    public SelectExecution<${execution.table.record.simpleName}> select() {
        final SelectExecution<${execution.table.record.simpleName}> execution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
            resultSet -> {
                return new ${execution.table.record.simpleName}(
                <#list execution.table.record.fields as field>
                    resultSetProcessor.getValue(resultSet, "${execution.table.metadata.name}", "${field.metadata.name}", ${execution.table.record.simpleName}.class, "${field.name}", ${field.type}.class)<#if field?has_next>,</#if>
                </#list>
                );
            }
        );

        execution.from(${execution.table.simpleName}.__table__);

        return execution;
    }

    @Nonnull
    public InsertExecution insert() {
        return new InsertExecution(executionConnection, sqlBuilder)
            .into(${execution.table.simpleName}.__table__);
    }

    @Nonnull
    public InsertExecution insert(@Nonnull final ${execution.table.record.simpleName} record) {
        final InsertExecution insert = new InsertExecution(executionConnection, sqlBuilder)
            .into(${execution.table.simpleName}.__table__)
            .columns(INSERT_FIELDS);

        final List<Object> values = new ArrayList<>();
        <#list execution.table.record.fields as field>
        values.add(record.${field.name}());
        </#list>
        insert.values(List.of(values));

        return insert;
    }

    @Nonnull
    public UpdateExecution update() {
        return new UpdateExecution(executionConnection, sqlBuilder)
            .table(${execution.table.simpleName}.__table__);
    }

    @Nonnull
    public UpdateExecution update(@Nonnull final ${execution.table.record.simpleName} record) {
        final UpdateExecution update = new UpdateExecution(executionConnection, sqlBuilder)
            .table(${execution.table.simpleName}.__table__);

        final List<Assignment> assignments = new ArrayList<>();
        <#list execution.table.record.fields as field>
        assignments.add(set(column("${field.metadata.name}"), record.${field.name}()));
        </#list>
        update.sets(assignments);

        update.where(
            <#list execution.table.record.primaryKeys as field>
            eq(column("${field.metadata.name}"), record.${field.name}())
            </#list>
        );

        return update;
    }

    @Nonnull
    public ListUpdateExecution<${execution.table.record.simpleName}> update(@Nonnull final List<${execution.table.record.simpleName}> records) {
        return new ListUpdateExecution<>(
            executionConnection,
            sqlBuilder,
            records,
            new ListUpdateExecution.Preprocess<>() {

                @Nonnull
                @Override
                public Update createUpdate() {
                    final List<Assignment> sets = new ArrayList<>(${execution.table.record.fields?size + execution.table.record.primaryKeys?size});
                    <#list execution.table.record.fields as field>
                    sets.add(set(column("${field.metadata.name}"), RawValue.rawValue("?")));
                    </#list>

                    return Update.update()
                        .table(${execution.table.simpleName}.__table__)
                        .sets(sets)
                        .where(
                        <#list execution.table.record.primaryKeys as field>
                            eq(column("${field.metadata.name}"), RawValue.rawValue("?"))
                        </#list>
                        );
                }

                @Nonnull
                @Override
                public Delete createDelete() {
                    return Delete.delete()
                        .from(${execution.table.simpleName}.__table__);
                }

                @Override
                public void setStatementObjects(
                    @Nonnull PreparedStatement preparedStatement,
                    @Nonnull ${execution.table.record.simpleName} record
                ) throws SQLException {
                    <#list execution.table.record.fields as field>
                    preparedStatement.setObject(${field?index + 1}, record.${field.name}());
                    </#list>
                    <#assign offset = execution.table.record.fields?size>
                    <#list execution.table.record.primaryKeys as field>
                    preparedStatement.setObject(${offset + field?index + 1}, record.${field.name}());
                    </#list>
                }
            }
        );
    }

    @Nonnull
    public DeleteExecution delete() {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .from(${execution.table.simpleName}.__table__);
    }

    @Nonnull
    public DeleteExecution delete(@Nonnull final ${execution.table.record.simpleName} record) {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .from(${execution.table.simpleName}.__table__)
            .where(
            <#list execution.table.record.primaryKeys as field>
                eq("${field.metadata.name}", record.${field.name}())
            </#list>
            );
    }
}