<#-- @ftlvariable name="tableCodeModel" type="io.datlin.rcm.TableCodeModel" -->
package ${tableCodeModel.packageName};

import io.datlin.sql.ast.ColumnReference;
import io.datlin.sql.ast.TableReference;

import static io.datlin.sql.ast.ColumnReference.column;
import static io.datlin.sql.ast.TableReference.table;

public class ${tableCodeModel.simpleName} {
    public static final TableReference ${tableCodeModel.field} = table("${tableCodeModel.tableMetadata.name}");
<#list tableCodeModel.columnCodeModels as columnCodeModel>
    public static final ColumnReference ${columnCodeModel.name} = column("${columnCodeModel.name}");
</#list>
}