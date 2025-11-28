package io.datlin.sql.metadata;

import java.util.List;

public record DatabaseMetadata(
    List<TableMetadata> tables
) {

}
