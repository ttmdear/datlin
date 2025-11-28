package io.datlin.sql.metadata;

public record DataType(
    String name,
    Engine engine
) {
}