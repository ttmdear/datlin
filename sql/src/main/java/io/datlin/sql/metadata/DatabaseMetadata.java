package io.datlin.sql.metadata;

import com.clever4j.lang.AllNonnullByDefault;

import java.util.List;

@AllNonnullByDefault
public record DatabaseMetadata(
    List<TableMetadata> tables
) {

}
