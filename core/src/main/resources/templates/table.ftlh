package ${table.packageName};

import io.datlin.sql.ast.ColumnReference;

import static io.datlin.sql.ast.ColumnReference.column;

public class ${table.simpleName} {
<#list table.columns as column>
    public static final ColumnReference ${column.name} = column("${column.name}");
</#list>
}