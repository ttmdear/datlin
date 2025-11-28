package io.datlin.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.annotation.Nonnull;

public class PathUtil {
    private static final Pattern ENV_VAR_UNIX = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final Pattern ENV_VAR_WIN = Pattern.compile("%([^%]+)%");

    public PathUtil() {
    }

    public @Nonnull Path expand(final @Nonnull Path path) {
        String pathString = path.toString();

        // user home ---------------------------------------------------------------------------------------------------
        if (pathString.startsWith("~")) {
            String home = System.getProperty("user.home");
            if (pathString.equals("~")) {
                pathString = home;
            } else if (pathString.startsWith("~/")) {
                pathString = home + pathString.substring(1);
            }
        }

        // ${UNIX} -----------------------------------------------------------------------------------------------------
        final Matcher unixMatcher = ENV_VAR_UNIX.matcher(pathString);
        final StringBuilder unixBuffer = new StringBuilder();
        while (unixMatcher.find()) {
            String value = System.getenv(unixMatcher.group(1));
            if (value == null) {
                value = "";
            }
            unixMatcher.appendReplacement(unixBuffer, Matcher.quoteReplacement(value));
        }
        unixMatcher.appendTail(unixBuffer);
        pathString = unixBuffer.toString();

        // %VAR% -------------------------------------------------------------------------------------------------------
        final Matcher windowMatcher = ENV_VAR_WIN.matcher(pathString);
        final StringBuilder windowBuffer = new StringBuilder();
        while (windowMatcher.find()) {
            String value = System.getenv(windowMatcher.group(1));
            if (value == null) {
                value = "";
            }
            windowMatcher.appendReplacement(windowBuffer, Matcher.quoteReplacement(value));
        }
        windowMatcher.appendTail(windowBuffer);
        pathString = windowBuffer.toString();

        return Paths.get(pathString).toAbsolutePath().normalize();
    }
}