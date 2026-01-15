package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class RepositoryCodeModelV1
{

    @Nonnull
    final String packageName;

    @Nonnull
    final String recordsPackageName;

    @Nonnull
    final String tablesPackageName;

    @Nonnull
    final String executionsPackageName;

    @Nullable
    DatabaseCodeModel database;

    @Nonnull
    final List<TableCodeModelV1> tables = new ArrayList<>();

    @Nonnull
    final List<RecordCodeModel> records = new ArrayList<>();

    @Nonnull
    final List<TableExecutionCodeModel> executions = new ArrayList<>();

    @Nonnull
    public DatabaseCodeModel getDatabase() {
        if (this.database == null) {
            throw new IllegalStateException("Database code model has not been initialized");
        }

        return database;
    }
}