package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Getter
@RequiredArgsConstructor
public class DatabaseCodeModel {

    @Nonnull
    final String simpleName;

    @Nonnull
    final String canonicalName;

    @Nonnull
    final String packageName;

    @Nonnull
    final RepositoryCodeModel repositoryCodeModel;

    @Nonnull
    final List<ExecutionCodeModel> executions = new ArrayList<>();

    @Nonnull
    public List<ExecutionCodeModel> getExecutions() {
        return unmodifiableList(executions);
    }
}
