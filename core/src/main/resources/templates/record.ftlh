package ${record.packageName};

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ${record.simpleName}(
<#list record.fields as field>
<#--    <#if field.primaryKey>@Id</#if>-->
<#--    <#if field.nullable>@Nullable<#else>@Nonnull</#if>-->
<#--    @Column(name = "${field.name}")-->
    <#if field.nullable>@Nullable<#else>@Nullable</#if> ${field.type.canonicalName} ${field.name}<#if field?has_next>,</#if>
</#list>
) {

}