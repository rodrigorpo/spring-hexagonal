package xyz.rpolnx.spring_hexagonal.infrastructure.util;

import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

public class UserFactory {
    public static User fromEntity(UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }
}
