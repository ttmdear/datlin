package io.datlin.sql.ast;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import static io.datlin.sql.ast.RawValue.rawValue;

/**
 * Represents a SQL function call consisting of a function name and its arguments.
 */
public record FunctionCall(
    @Nonnull String function,
    @Nonnull List<SqlFragment> arguments,
    @Nullable String alias
) implements SqlFragment, Aliasable<FunctionCall> {

    @Nonnull
    @Override
    public FunctionCall as(@Nonnull final String alias) {
        return new FunctionCall(function, arguments, alias);
    }

    /**
     * Creates a new FunctionCall instance.
     *
     * @param function  the name of the SQL function (e.g., "SUM", "COUNT")
     * @param arguments a list of {@link SqlFragment} representing the function's arguments
     * @return a new FunctionCall instance
     */
    @Nonnull
    public static FunctionCall call(
        @Nonnull final String function,
        @Nonnull final List<SqlFragment> arguments
    ) {
        return new FunctionCall(function, arguments, null);
    }

    /**
     * Creates a new FunctionCall instance using varargs for arguments.
     *
     * @param function  the name of the SQL function
     * @param arguments variable number of {@link SqlFragment} arguments
     * @return a new FunctionCall instance
     */
    @Nonnull
    public static FunctionCall call(
        @Nonnull final String function,
        @Nonnull final SqlFragment... arguments
    ) {
        return new FunctionCall(function, Arrays.stream(arguments).toList(), null);
    }

    /**
     * Creates a COUNT(argument) function call.
     *
     * @param argument the fragment to count
     * @return a FunctionCall representing the COUNT aggregate
     */
    @Nonnull
    public static FunctionCall count(@Nonnull final SqlFragment argument) {
        return call("COUNT", argument);
    }

    /**
     * Creates a COUNT(*) function call to count all rows.
     *
     * @return a FunctionCall representing COUNT(*)
     */
    @Nonnull
    public static FunctionCall countAll() {
        return call("COUNT", rawValue("*"));
    }

    /**
     * Creates a SUM(argument) function call.
     *
     * @param argument the numeric fragment to sum
     * @return a FunctionCall representing the SUM aggregate
     */
    @Nonnull
    public static FunctionCall sum(@Nonnull final SqlFragment argument) {
        return call("SUM", argument);
    }

    /**
     * Creates an AVG(argument) function call to calculate the average value.
     *
     * @param argument the numeric fragment to average
     * @return a FunctionCall representing the AVG aggregate
     */
    @Nonnull
    public static FunctionCall avg(@Nonnull final SqlFragment argument) {
        return call("AVG", argument);
    }

    /**
     * Creates a MIN(argument) function call to find the minimum value.
     *
     * @param argument the fragment to evaluate
     * @return a FunctionCall representing the MIN aggregate
     */
    @Nonnull
    public static FunctionCall min(@Nonnull final SqlFragment argument) {
        return call("MIN", argument);
    }

    /**
     * Creates a MAX(argument) function call to find the maximum value.
     *
     * @param argument the fragment to evaluate
     * @return a FunctionCall representing the MAX aggregate
     */
    @Nonnull
    public static FunctionCall max(@Nonnull final SqlFragment argument) {
        return call("MAX", argument);
    }

    /**
     * Creates an UPPER(argument) function call to convert text to uppercase.
     *
     * @param argument the text fragment
     * @return a FunctionCall representing the UPPER function
     */
    @Nonnull
    public static FunctionCall upper(@Nonnull final SqlFragment argument) {
        return call("UPPER", argument);
    }

    /**
     * Creates a LOWER(argument) function call to convert text to lowercase.
     *
     * @param argument the text fragment
     * @return a FunctionCall representing the LOWER function
     */
    @Nonnull
    public static FunctionCall lower(@Nonnull final SqlFragment argument) {
        return call("LOWER", argument);
    }

    /**
     * Creates a COALESCE(args...) function call to return the first non-null value.
     *
     * @param arguments variable number of fragments to check
     * @return a FunctionCall representing the COALESCE function
     */
    @Nonnull
    public static FunctionCall coalesce(@Nonnull final SqlFragment... arguments) {
        return call("COALESCE", arguments);
    }

    /**
     * Creates a CONCAT(args...) function call to concatenate multiple text fragments.
     *
     * @param arguments variable number of fragments to concatenate
     * @return a FunctionCall representing the CONCAT function
     */
    @Nonnull
    public static FunctionCall concat(@Nonnull final SqlFragment... arguments) {
        return call("CONCAT", arguments);
    }

    /**
     * Creates a NOW() function call to return the current database timestamp.
     *
     * @return a FunctionCall representing the NOW function
     */
    @Nonnull
    public static FunctionCall now() {
        return call("NOW", List.of());
    }
}
