<#-- @ftlvariable name="recordCodeModel" type="io.datlin.rcm.RecordCodeModel" -->
package ${recordCodeModel.packageName};

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "${recordCodeModel.table}")
public record ${recordCodeModel.simpleName}(
<#list recordCodeModel.fields as field>
    <#if field.primaryKey>@Id</#if>
    <#if field.nullable>@Column(name = "${field.columnMetadata.name}")<#else>@Column(name = "${field.columnMetadata.name}", nullable = false)</#if>
    <#if field.nullable>@Nullable<#else>@Nonnull</#if> ${field.type} ${field.name}<#if field?has_next>,</#if>
</#list>
) {

}