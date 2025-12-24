package io.datlin.sql.mtd;

import jakarta.annotation.Nonnull;

import java.util.List;

public record DatabaseMetadata(
    @Nonnull List<TableMetadata> tables
) {

}
