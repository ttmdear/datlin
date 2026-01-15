package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("ClassCanBeRecord")
@RequiredArgsConstructor
@Getter
public class TableExecutionCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    // todo clean
    // @Nonnull
    // final String methodName;

    @Nonnull
    final TableCodeModel table;

    @Nonnull
    final String resultSetProcessor;
}
