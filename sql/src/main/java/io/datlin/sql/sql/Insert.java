package io.datlin.sql.sql;

import com.clever4j.lang.AllNonnullByDefault;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@AllNonnullByDefault
public final class Insert implements Expression {

    @Nullable
    Identifier into;

    List<String> columns = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    Insert() {

    }

    public static Insert build() {
        return new Insert();
    }

    // into ------------------------------------------------------------------------------------------------------------
    public Insert into(String into) {
        this.into = Identifier.of(into);
        return this;
    }

    // value -----------------------------------------------------------------------------------------------------------
    public <T> Insert value(String column, T value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    public Insert clear() {
        columns.clear();
        values.clear();
        return this;
    }
}