package io.datlin.sql.metadata;

import com.clever4j.lang.AllNonnullByDefault;

@AllNonnullByDefault
public record DataType(
    String name,
    Engine engine
) {
}