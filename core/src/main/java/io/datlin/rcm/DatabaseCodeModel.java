package io.datlin.rcm;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    final RepositoryCodeModel repository;

    @Nonnull
    final List<TableCodeModel> tables = new ArrayList<>();
}