<#-- @ftlvariable name="database" type="io.datlin.rcm.DatabaseCodeModel" -->
package ${database.packageName};

import io.datlin.sql.bld.GenericSqlBuilder;
import io.datlin.sql.con.ConnectionProvider;
import io.datlin.sql.exe.ExecutionConnection;
import jakarta.annotation.Nonnull;

public class ${database.simpleName} {

    @Nonnull
    private final ConnectionProvider connectionProvider;

    @Nonnull
    private final GenericSqlBuilder sqlBuilder;

    public ${database.simpleName}(
        @Nonnull final ConnectionProvider connectionProvider
    ) {
        this.connectionProvider = connectionProvider;
        this.sqlBuilder = new GenericSqlBuilder();
    }

<#--<#list database.executions as execution>-->
<#--    @Nonnull-->
<#--    public ${execution.canonicalName} ${execution.methodName}() {-->
<#--        return new ${execution.canonicalName}(-->
<#--            new ExecutionConnection(connectionProvider::getDefaultConnection),-->
<#--            sqlBuilder-->
<#--        );-->
<#--    }-->

<#--</#list>-->
    @Nonnull
    public TransactionExecution beginTransaction() {
        return new TransactionExecution();
    }

    public class TransactionExecution {

        private TransactionExecution() {

        }

<#--    <#list database.executions as execution>-->
<#--        @Nonnull-->
<#--        public ${execution.canonicalName} ${execution.methodName}() {-->
<#--            return new ${execution.canonicalName}(-->
<#--                new ExecutionConnection(connectionProvider::getConnection),-->
<#--                sqlBuilder-->
<#--            );-->
<#--        }-->

<#--    </#list>-->
    }
}