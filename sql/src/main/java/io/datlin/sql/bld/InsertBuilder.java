package io.datlin.sql.bld;

import io.datlin.sql.ast.InsertNode;
import io.datlin.sql.ast.TableLiteralNode;
import io.datlin.sql.exc.InsertIntoNotSetException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InsertBuilder {

    @Nonnull
    private final List<String> fields = new ArrayList<>();

    @Nonnull
    private final List<List<Object>> values = new ArrayList<>();

    @Nullable
    private TableLiteralNode into;

    @Nonnull
    public InsertBuilder into(
        @Nonnull final String name
    ) {
        this.into = new TableLiteralNode(null, name);
        return this;
    }

    @Nonnull
    public InsertBuilder into(
        @Nonnull final String schema,
        @Nonnull final String name
    ) {
        this.into = new TableLiteralNode(schema, name);
        return this;
    }

    @Nonnull
    public InsertBuilder fields(
        @Nonnull final List<String> fields
    ) {
        this.fields.clear();
        this.fields.addAll(fields);
        return this;
    }

    @Nonnull
    public InsertBuilder fields(
        @Nonnull final String... fields
    ) {
        this.fields.clear();
        this.fields.addAll(List.of(fields));
        return this;
    }

    @Nonnull
    public InsertBuilder values(
        @Nonnull final List<Object> values
    ) {
        this.values.add(values);
        return this;
    }

    @Nonnull
    public InsertBuilder values(
        @Nonnull final Object... values
    ) {
        this.values.add(List.of(values));
        return this;
    }

    @Nonnull
    public InsertNode build() {
        if (this.into == null) {
            throw new InsertIntoNotSetException();
        }

        return new InsertNode(fields, values, into);
    }
}
