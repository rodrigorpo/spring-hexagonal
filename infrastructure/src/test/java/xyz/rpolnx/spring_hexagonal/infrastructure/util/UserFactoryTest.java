package xyz.rpolnx.spring_hexagonal.infrastructure.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

public class UserFactoryTest {

    @Test
    @DisplayName("When passing an UserEntity, should generate filled User")
    public void shouldGenerateUserFromEntity() {
        UserEntity entity = new UserEntity(UUID.randomUUID(), "Test case");

        User actual = UserFactory.fromEntity(entity);
        User expected = new User(entity.getId(), entity.getName());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When passing a null entity, should generate a null User")
    public void shouldGenerateNullUser() {
        User actual = UserFactory.fromEntity(null);

        assertNull(actual);
    }
}