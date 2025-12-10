package ${record.packageName};

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "${record.table}")
public record ${record.simpleName}(
<#list record.fields as field>
    <#if field.primaryKey>@Id</#if>
    <#if field.nullable>@Column(name = "${field.columnName}")<#else>@Column(name = "${field.columnName}", nullable = false)</#if>
    <#if field.nullable>@Nullable<#else>@Nullable</#if> ${field.type} ${field.name}<#if field?has_next>,</#if>
</#list>
) {

}