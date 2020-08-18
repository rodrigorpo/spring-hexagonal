package xyz.rpolnx.spring_hexagonal.infrastructure.util;

import java.util.Objects;

import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

public class UserFactory {
    public static User fromEntity(UserEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return new User(entity.getId(), entity.getName());
    }
}
