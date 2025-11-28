package io.datlin.sql.metadata;

import com.clever4j.lang.AllNonnullByDefault;

@AllNonnullByDefault
public record ColumnMetadata(
    String name,
    boolean primaryKey,
    String type,
    boolean nullable
) {
}