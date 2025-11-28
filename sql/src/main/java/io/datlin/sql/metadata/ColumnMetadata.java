package io.datlin.sql.metadata;

public record ColumnMetadata(
    String name,
    boolean primaryKey,
    String type,
    boolean nullable
) {
}