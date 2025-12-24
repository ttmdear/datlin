package ${executionCodeModel.packageName};

import java.util.ArrayList;
import java.util.List;

import io.datlin.sql.bld.SqlBuilder;
import io.datlin.sql.exe.DeleteExecution;
import io.datlin.sql.exe.ExecutionConnection;
import io.datlin.sql.exe.InsertExecution;
import io.datlin.sql.exe.SelectExecution;
import io.datlin.sql.exe.UpdateExecution;
import io.datlin.sql.rsp.ResultSetProcessor;
import jakarta.annotation.Nonnull;
import ${executionCodeModel.recordCodeModel.canonicalName};

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
        INSERT_FIELDS.add("${field.columnName}");
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
        final SelectExecution<${executionCodeModel.recordCodeModel.simpleName}> selectExecution = new SelectExecution<>(
            executionConnection,
            sqlBuilder,
            resultSet -> {
                return new ${executionCodeModel.recordCodeModel.simpleName}(
                <#list executionCodeModel.recordCodeModel.fields as field>
                    resultSetProcessor.getValue(resultSet, "${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnName}", ${executionCodeModel.recordCodeModel.simpleName}.class, "${field.name}", ${field.type}.class)<#if field?has_next>,</#if>
                </#list>
                );
            }
        );

        selectExecution.from("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}", "t1");

        return selectExecution;
    }

    @Nonnull
    public InsertExecution insert() {
        return new InsertExecution(executionConnection, sqlBuilder)
            .into("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}");
    }

    @Nonnull
    public InsertExecution insert(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final InsertExecution insert = new InsertExecution(executionConnection, sqlBuilder)
            .into("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}")
            .fields(INSERT_FIELDS);

        final List<Object> values = new ArrayList<>();
        <#list executionCodeModel.recordCodeModel.fields as field>
        values.add(record.${field.name}());
        </#list>
        insert.values(values);

        return insert;
    }

    @Nonnull
    public UpdateExecution update() {
        return new UpdateExecution(executionConnection, sqlBuilder)
            .table("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}");
    }

    @Nonnull
    public UpdateExecution update(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final UpdateExecution update = new UpdateExecution(executionConnection, sqlBuilder)
            .table("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}");

        <#list executionCodeModel.recordCodeModel.fields as field>
        update.set("${field.columnName}", record.${field.name}());
        </#list>
        update.where(where -> {
            <#list executionCodeModel.recordCodeModel.primaryKeys as field>
            where.eq("${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnName}", record.${field.name}());
            </#list>
        });

        return update;
    }

    @Nonnull
    public DeleteExecution delete() {
        return new DeleteExecution(executionConnection, sqlBuilder)
            .table("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}");
    }

    @Nonnull
    public DeleteExecution delete(@Nonnull final ${executionCodeModel.recordCodeModel.simpleName} record) {
        final DeleteExecution delete = new DeleteExecution(executionConnection, sqlBuilder)
            .table("public", "${executionCodeModel.tableCodeModel.tableMetadata.name}");

        delete.where(where -> {
        <#list executionCodeModel.recordCodeModel.primaryKeys as field>
            where.eq("${executionCodeModel.tableCodeModel.tableMetadata.name}", "${field.columnName}", record.${field.name}());
        </#list>
        });

        return delete;
    }
}