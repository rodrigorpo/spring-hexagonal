package xyz.rpolnx.spring_hexagonal.application.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import xyz.rpolnx.spring_hexagonal.domain.model.User;

public class UserDTOTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    @DisplayName("When attribute is null, should not show on serialization")
    public void shouldSerializeCorrectly() {
        UserDTO userDTO = UserDTO.fromUser(new User(null, "test name"));
        String current = mapper.writeValueAsString(userDTO);

        String expected = "{\"name\":\"test name\"}";

        assertEquals(expected, current);
    }

    @Test
    @DisplayName("When passing User, should return new instance of UserDto")
    public void shouldCreateUserFromUser() {
        UUID id = UUID.randomUUID();
        UserDTO current = UserDTO.fromUser(new User(id, "test name"));

        UserDTO expected = new UserDTO();
        expected.setId(id);
        expected.setName("test name");

        assertEquals(expected, current);
    }

    @Test
    @DisplayName("When user is null, should dto should be null")
    public void shouldReturnNull() {
        assertNull(UserDTO.fromUser(null));
    }

    @Test
    @DisplayName("When calling method, should convert itself to User")
    public void shouldConvertDtoToUser() {
        UUID id = UUID.randomUUID();
        UserDTO userDTO = UserDTO.fromUser(new User(id, "test name"));
        User user = Objects.requireNonNull(userDTO).toUser();
        assertEquals(new User(id, "test name"), user);

        UserDTO userDtoWithAttributesNull = UserDTO.fromUser(new User(null, null));
        User userWithAttributesNull = Objects.requireNonNull(userDtoWithAttributesNull).toUser();
        assertEquals(new User(null, null), userWithAttributesNull);

        UserDTO nullUserDto = null;
        assertThrows(NullPointerException.class, () -> nullUserDto.toUser());
    }

}