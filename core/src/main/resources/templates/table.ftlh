package ${table.packageName};

public class ${table.simpleName} {
<#list table.columns as column>
    public static final String ${column.name} = "${column.name}";
</#list>
}