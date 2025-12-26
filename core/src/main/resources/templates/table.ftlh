<#-- @ftlvariable name="table" type="io.datlin.rcm.TableCodeModel" -->
package ${table.packageName};

import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.TableReference;

import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.TableReference.table;

public class ${table.simpleName} {
    public static final TableReference ${table.metadata.name} = table("${table.metadata.name}");
<#list table.columns as column>
    public static final ColumnReference ${column.name} = column("${column.name}");
</#list>
}