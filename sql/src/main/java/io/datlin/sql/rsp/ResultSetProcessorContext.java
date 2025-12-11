package io.datlin.sql.rsp;

import jakarta.annotation.Nonnull;

public record ResultSetProcessorContext(
    @Nonnull String table,
    @Nonnull String column
) {

}
