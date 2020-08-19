package xyz.rpolnx.spring_hexagonal.application.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

public class SerializationConfigTest {
    private final ObjectMapper mapper = new SerializationConfig().mapper();

    @SneakyThrows
    @Test
    @DisplayName("When serializing object, should serialize date in UTC pattern")
    public void t() {
        UUID id = UUID.randomUUID();
        LocalDate birthday = LocalDate.of(2010, 10, 2);
        User user = new User(id, "Timestamp serialization", birthday);

        String json = mapper.writeValueAsString(user);

        String birthdayString = json.split(",")[2].split("\"")[3];
        String createdAtString = json.split(",")[3].split("\"")[3];

        assertTrue(birthdayString.matches("^\\d{4}-\\d{2}-\\d{2}$"));
        assertTrue(createdAtString.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}$"));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class User {
        private UUID id;
        private String name;
        private LocalDate birthday;
        private final LocalDateTime createdAt = LocalDateTime.now();
    }
}