package io.datlin.sql.exe;

import jakarta.annotation.Nonnull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class StatementObjectBinder {

    public void bind(
        @Nonnull final PreparedStatement statement,
        @Nonnull final List<Object> objects
    ) {
        try {
            for (int i = 0; i < objects.size(); i++) {
                final Object value = objects.get(i);

                if (value instanceof Instant instant) {
                    statement.setTimestamp(i + 1, Timestamp.from(instant));
                } else {
                    statement.setObject(i + 1, value);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
