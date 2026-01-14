<#-- @ftlvariable name="table" type="io.datlin.rcm.TableCodeModelV1" -->
package ${table.packageName};

import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.TableReference;

import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.TableReference.table;

public class ${table.simpleName} {

    public static final TableReference ${table.tableReferenceField} = table("${table.metadata.name}");

<#list table.columns as column>
    public static final ColumnReference ${column.name} = column("${column.name}");
    public static final boolean ${column.name}_nullable = ${column.nullable?string("true", "false")};
    <#if column.metadata.length?has_content>
    public static final int ${column.name}_length = ${column.metadata.length?c};
    </#if>
    <#if column.metadata.precision?has_content>
        public static final int ${column.name}_precision = ${column.metadata.precision?c};
    </#if>
    <#if column.metadata.scale?has_content>
        public static final int ${column.name}_scale = ${column.metadata.scale?c};
    </#if>

</#list>
}