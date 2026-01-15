package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RepositoryCodeModel {

    @Nonnull
    final String packageName;

    @Nonnull
    final String tablesPackageName;

    @Nullable
    DatabaseCodeModel database;

    @Nonnull
    public DatabaseCodeModel getDatabase() {
        if (this.database == null) {
            throw new IllegalStateException("Database code model has not been initialized");
        }

        return database;
    }
}