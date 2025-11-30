<#ftl output_format='plainText'>
package ${implementationDaoTemplate.packageName};

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clever4j.rdb.sql.BuildContext;
import com.clever4j.rdb.connection.ConnectionProvider;
import com.clever4j.rdb.sql.Insert;
import com.clever4j.rdb.sql.Delete;
import com.clever4j.rdb.sql.Update;
import com.clever4j.rdb.sql.Select;
import com.clever4j.rdb.sql.OrderBy;
import com.clever4j.rdb.sql.PostgreSqlBuilder;
import java.util.NoSuchElementException;

import ${record.name()};

@SuppressWarnings({"DataFlowIssue", "unused", "DuplicatedCode", "SqlSourceToSinkFlow"})
public abstract class ${implementationDaoTemplate.simpleName} {

    protected final ConnectionProvider connectionProvider;
    protected final PostgreSqlBuilder builder = new PostgreSqlBuilder();

    public ${implementationDaoTemplate.simpleName}(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public List<${record.simpleName()}> getAll() {
        return getAll(connectionProvider.getConnection("${database.id()}"));
    }

    public List<${record.simpleName()}> getAll(Connection connection) {
        Select select = buildSelect();

        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);
        return fetch(sql, context, connection);
    }

    // getAll ordered --------------------------------------------------------------------------------------------------
    public List<${record.simpleName()}> getAll(OrderBy orderBy) {
        return getAll(connectionProvider.getConnection("${database.id()}"));
    }

    public List<${record.simpleName()}> getAll(OrderBy orderBy, Connection connection) {
        Select select = buildSelect()
            .orderBy(orderBy);

        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);
        return fetch(sql, context, connection);
    }

    // getById ---------------------------------------------------------------------------------------------------------
    @jakarta.annotation.Nonnull
    public ${record.simpleName()} getById(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>) {
        return getById(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>, </#if></#list>, connectionProvider.getConnection("${database.id()}"));
    }

    @jakarta.annotation.Nonnull
    public ${record.simpleName()} getById(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>, Connection connection) {
        Select select = buildSelect()
            .where(where -> {
            <#list record.primaryKeys() as f>
                where.equal("${f.columnName()}", ${f.name()});
            </#list>
            });

        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);
        return fetchOne(sql, context, connection);
    }

    // requireById -----------------------------------------------------------------------------------------------------
    public ${record.simpleName()} requireById(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>) {
        return requireById(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>, </#if></#list>, connectionProvider.getConnection("${database.id()}"));
    }

    public ${record.simpleName()} requireById(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>, Connection connection) {
        ${record.simpleName()} record = getById(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>, </#if></#list>, connection);

        if (record == null) {
            throw new NoSuchElementException("${record.simpleName()} with id <#list record.primaryKeys() as f>%s<#if f?has_next>, </#if></#list>.".formatted(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>, </#if></#list>));
        }

        return record;
    }

    // getByIn ---------------------------------------------------------------------------------------------------------
    <#list record.fields() as f>
    protected List<${record.simpleName()}> getBy${f.pascalName()}In(List<${f.type().getName()}> in, OrderBy orderBy, Connection connection) {
        Select select = buildSelect()
            .where(where -> {
                where.in("${f.columnName()}", in);
            })
            .orderBy(orderBy);

        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);

        return fetch(sql, context, connection);
    }

    </#list>
    // -----------------------------------------------------------------------------------------------------------------
    protected Select buildSelect() {
        return Select.build()
        <#list record.fields() as f>
            .column("${f.columnName()}")
        </#list>
            .from("${record.table().name()}");
    }

    @jakarta.annotation.Nonnull
    protected ${record.simpleName()} extractOne(List<${record.simpleName()}> records) {
        if (records.isEmpty()) {
            throw new NoSuchElementException("${record.simpleName()} not found.");
        } else if (records.size() == 1) {
            return records.getFirst();
        } else {
            throw new IllegalStateException("More then one ${record.simpleName()} record");
        }
    }

    // fetch -----------------------------------------------------------------------------------------------------------
    @jakarta.annotation.Nonnull
    protected ${record.simpleName()} fetchOne(Select select, Connection connection) {
        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);
        return fetchOne(sql, context, connection);
    }

    @jakarta.annotation.Nonnull
    protected ${record.simpleName()} fetchOne(String query, BuildContext context, Connection connection) {
        List<${record.simpleName()}> result = fetch(query, context, connection);

        if (result.isEmpty()) {
            throw new NoSuchElementException("${record.simpleName()} not found.");
        } else if (result.size() == 1) {
            return result.getFirst();
        } else {
            throw new NoSuchElementException("More then one ${record.simpleName()} record");
        }
    }

    protected List<${record.simpleName()}> fetch(Select select, Connection connection) {
        BuildContext context = new BuildContext();
        String sql = builder.build(select, context);
        return fetch(sql, context, connection);
    }

    protected List<${record.simpleName()}> fetch(String query, BuildContext context, Connection connection) {
        List<${record.simpleName()}> result = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            context.prepareStatement(statement);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(new ${record.simpleName()} (
                <#list record.fields() as field>
                    ${generateCreateJavaType(field)}<#if field?has_next>,</#if>
                </#list>
                ));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ${record.simpleName()} mapResultSet(ResultSet resultSet) {
        try {
            return new ${record.simpleName()} (
            <#list record.fields() as field>
                ${generateCreateJavaType(field)}<#if field?has_next>,</#if>
            </#list>
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // INSERT ----------------------------------------------------------------------------------------------------------
    public void insert(${record.simpleName()} record) {
        insert(record, connectionProvider.getConnection("${database.id()}"));
    }

    public void insert(${record.simpleName()} record, Connection connection) {
        Insert insert = Insert.build()
            .into("${record.table().name()}")
            <#list record.fields() as f>
            .value("${f.columnName()}", record.${f.name()}())
            </#list>
        ;

        BuildContext context = new BuildContext();
        String sql = builder.build(insert, context);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // delete ----------------------------------------------------------------------------------------------------------
    public void delete(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>) {
        delete(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>,</#if></#list>, connectionProvider.getConnection("${database.id()}"));
    }

    public void delete(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>, Connection connection) {
        Delete delete = Delete.build()
            .table("${record.table().name()}")
            .where(where -> {
            <#list record.primaryKeys() as f>
                where.equal("${f.columnName()}", ${f.name()});
            </#list>
            });

        BuildContext context = new BuildContext();
        String sql = builder.build(delete, context);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // deleteAll -------------------------------------------------------------------------------------------------------
    public void deleteAll() {
        deleteAll(connectionProvider.getConnection("${database.id()}"));
    }

    public void deleteAll(Connection connection) {
        Delete delete = Delete.build()
            .table("${record.table().name()}");

        BuildContext context = new BuildContext();
        String sql = builder.build(delete, context);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // deleteBy --------------------------------------------------------------------------------------------------------
    <#list record.fields() as f>
    protected void deleteBy${f.pascalName()}In(List<${f.canonicalNameType}> in, Connection connection) {
        Delete delete = Delete.build()
            .table("${record.table.name}")
            .where(where -> {
                where.in("${f.columnName()}", in);
            });

        BuildContext context = new BuildContext();
        String sql = builder.build(delete, context);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    </#list>

    // update ----------------------------------------------------------------------------------------------------------
    public void update(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>, ${record.simpleName()} record) {
        update(<#list record.primaryKeys() as f>${f.name()}<#if f?has_next>, </#if></#list>, record, connectionProvider.getConnection("${database.id()}"));
    }

    public void update(<#list record.primaryKeys() as f>${f.type().getName()} ${f.name()}<#if f?has_next>,</#if></#list>, ${record.simpleName()} record, Connection connection) {
        Update update = Update.build()
            .table("${record.table().name()}")
            <#list record.fields() as f>
            .set("${f.columnName()}", record.${f.name()}())
            </#list>
            .where(where -> {
            <#list record.primaryKeys() as f>
                where.equal("${f.columnName()}", ${f.name()});
            </#list>
            });

        BuildContext context = new BuildContext();
        String sql = builder.build(update, context);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            context.prepareStatement(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // connection ------------------------------------------------------------------------------------------------------
    protected Connection getConnection() {
        return connectionProvider.getConnection("${database.id()}");
    }

    // -----------------------------------------------------------------------------------------------------------------
}