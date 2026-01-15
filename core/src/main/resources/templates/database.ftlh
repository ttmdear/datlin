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


    @Nonnull
    public Tables tables() {
        return new Tables();
    }

    @Nonnull
    public TransactionExecution beginTransaction() {
        return new TransactionExecution();
    }

    public class Tables {

        private Tables() {

        }

        <#list database.tables as table>
        @Nonnull
        public ${table.execution.canonicalName} ${table.execution.databaseMethodName}() {
            return new ${table.execution.canonicalName}(
                new ExecutionConnection(connectionProvider::getDefaultConnection),
                sqlBuilder
            );
        }
        <#sep>

        </#sep>
        </#list>
    }

    public class TransactionExecution {

        private TransactionExecution() {

        }

        <#list database.tables as table>
        @Nonnull
        public ${table.execution.canonicalName} ${table.execution.databaseMethodName}() {
            return new ${table.execution.canonicalName}(
                new ExecutionConnection(connectionProvider::getConnection),
                sqlBuilder
            );
        }
        <#sep>

        </#sep>
        </#list>
    }
}