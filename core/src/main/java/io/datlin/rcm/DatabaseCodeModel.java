package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Getter
@RequiredArgsConstructor
public class DatabaseCodeModel {

    @Nonnull
    private final String simpleName;

    @Nonnull
    private final String canonicalName;

    @Nonnull
    private final String packageName;

    @Nonnull
    private final RepositoryCodeModel repository;

    @Nonnull
    private final List<ExecutionCodeModel> executions = new ArrayList<>();

    @Nonnull
    public List<ExecutionCodeModel> getExecutions() {
        return unmodifiableList(executions);
    }
}
