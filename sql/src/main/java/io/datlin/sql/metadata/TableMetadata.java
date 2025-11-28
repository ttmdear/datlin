package io.datlin.sql.metadata;

import java.util.List;

public record TableMetadata(
    String name,
    Engine engine,
    List<ColumnMetadata> columns
) {
}
