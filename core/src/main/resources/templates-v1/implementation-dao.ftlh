<#ftl output_format='plainText'>
package ${packageName};

import com.clever4j.rdb.connection.ConnectionProvider;
import ${implementationDaoTemplateModel.name()};
<#list database.implementationDaoAnnotations() as a>@${a}</#list>
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ${simpleName} extends ${implementationDaoTemplateModel.simpleName()} implements ${daoModel.simpleName()} {
    public ${simpleName}(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }
}