package io.datlin.sql.sql;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Identifier implements Expression {

    private static final Map<String, Identifier> INSTANCES = new ConcurrentHashMap<>();

    final String qualifier;
    final String identifier;

    private Identifier(String qualifier, String identifier) {
        this.qualifier = qualifier;
        this.identifier = identifier;
    }

    public static Identifier of(String identifier) {
        return INSTANCES.computeIfAbsent(identifier, identifier1 -> {
            String[] parts = identifier.split("\\.");

            if (parts.length == 1) {
                return new Identifier("", parts[0]);
            } else if (parts.length == 2) {
                return new Identifier(parts[0], parts[1]);
            } else {
                throw new IllegalArgumentException("Incorrect qualifier identifier: " + identifier);
            }
        });
    }

    public static Identifier of(String qualifier, String identifier) {
        return INSTANCES.computeIfAbsent(qualifier + "." + identifier,
            expression1 -> new Identifier(qualifier, identifier));
    }

    // toString, equals, hashCode --------------------------------------------------------------------------------------
    @Override
    public String toString() {
        if (qualifier.isEmpty()) {
            return identifier;
        } else {
            return qualifier + "." + identifier;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(qualifier, that.qualifier) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifier, identifier);
    }
}
