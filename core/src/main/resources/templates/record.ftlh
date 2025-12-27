<#-- @ftlvariable name="record" type="io.datlin.rcm.RecordCodeModel" -->
package ${record.packageName};

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "${record.table.metadata.name}")
public record ${record.simpleName}(
<#list record.fields as field>
    <#if field.primaryKey>@Id</#if>
    <#if field.nullable>@Nullable<#else>@Nonnull</#if>
    @Column(name = "${field.name}"<#rt>
        <#if field.nullable == false>, nullable = false</#if><#t>
        <#if field.metadata.length?has_content && field.metadata.length != 255>, length = ${field.metadata.length?c}</#if><#t>
        <#if field.metadata.precision?has_content>, precision = ${field.metadata.precision?c}</#if><#t>
        <#if field.metadata.scale?has_content>, scale = ${field.metadata.scale?c}</#if><#t>
    <#lt>)
    ${field.type} ${field.name}<#if field?has_next>,</#if>
</#list>
) {

}