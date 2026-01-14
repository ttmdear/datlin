package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("ClassCanBeRecord")
@RequiredArgsConstructor
@Getter
public class ExecutionCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    @Nonnull
    final String methodName;

    @Nonnull
    final String resultSetProcessor;

    @Nonnull
    final TableCodeModelV1 table;

    @Nonnull
    final RecordCodeModel record;
}
