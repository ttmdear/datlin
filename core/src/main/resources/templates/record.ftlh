package ${record.packageName};

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.List;
<#list database.recordAnnotations as a>@${a}</#list>
@Table(name = "${record.table.name}")
public record ${record.simpleName}(
<#list record.fields as field>

    <#if field.primaryKey>@Id</#if>
    <#if field.nullable>@Nullable<#else>@Nonnull</#if>
    @Column(name = "${field.name}")
    ${field.canonicalNameType} ${field.name}<#if field?has_next>,</#if>
</#list>
) {
<#list record.fields as f>
    public static final String _${f.name} = "${f.columnName}";
</#list>

    @Nonnull
    public static ${record.simpleName} requireById(List<${record.simpleName}> records, <#list record.primaryKeys as f>${f.canonicalNameType} ${f.name}<#if f?has_next>, </#if></#list>) {
        ${record.simpleName} record = getById(records, <#list record.primaryKeys as f>${f.name}<#if f?has_next>, </#if></#list>);

        if (record == null) {
            throw new IllegalStateException("${record.simpleName} with id <#list record.primaryKeys as f>%s<#if f?has_next>, </#if></#list>.".formatted(<#list record.primaryKeys as f>${f.name}<#if f?has_next>, </#if></#list>));
        }

        return record;
    }

    @Nullable
    public static ${record.simpleName} getById(List<${record.simpleName}> records, <#list record.primaryKeys as f>${f.canonicalNameType} ${f.name}<#if f?has_next>, </#if></#list>) {
        return records.stream()
        <#list record.primaryKeys as f>
            .filter(record -> record.${f.name}.equals(${f.name}))
        </#list>
            .findFirst().orElse(null);
    }
}