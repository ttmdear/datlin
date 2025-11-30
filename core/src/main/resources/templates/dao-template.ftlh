<#ftl output_format='plainText'>
package ${packageName};

import ${recordModel.name()};

import java.sql.Connection;
import java.util.List;

<#list database.implementationDaoAnnotations() as a>@${a}</#list>
@SuppressWarnings({"unused", "DuplicatedCode"})
public interface ${simpleName} {

    List<${recordModel.simpleName()}> getAll();

    List<${recordModel.simpleName()}> getAll(Connection connection);

    @jakarta.annotation.Nonnull
    ${recordModel.simpleName()} getById(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>);

    @jakarta.annotation.Nonnull
    ${recordModel.simpleName()} getById(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>, Connection connection);

    ${recordModel.simpleName()} requireById(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>);

    ${recordModel.simpleName()} requireById(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>, Connection connection);

    // INSERT ----------------------------------------------------------------------------------------------------------
    void insert(${recordModel.simpleName()} record);

    void insert(${recordModel.simpleName()} record, Connection connection);

    // DELETE ----------------------------------------------------------------------------------------------------------
    void delete(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>);

    void delete(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>, Connection connection);

    void deleteAll();

    void deleteAll(Connection connection);

    // UPDATE ----------------------------------------------------------------------------------------------------------
    void update(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>, ${recordModel.simpleName()} record);

    void update(<#list recordModel.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>, </#if></#list>, ${recordModel.simpleName()} record, Connection connection);
}