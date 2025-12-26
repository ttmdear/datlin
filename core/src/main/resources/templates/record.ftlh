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
    <#if field.nullable>@Column(name = "${field.metadata.name}")<#else>@Column(name = "${field.metadata.name}", nullable = false)</#if>
    <#if field.nullable>@Nullable<#else>@Nonnull</#if> ${field.type} ${field.name}<#if field?has_next>,</#if>
</#list>
) {

}