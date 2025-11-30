<#ftl output_format='plainText'>
package ${packageName};

import ${templateDaoModel.name()};

<#list database.recordAnnotations() as a>@${a}</#list>
@SuppressWarnings({"unused"})
public interface ${simpleName} extends ${templateDaoModel.simpleName()} {

}