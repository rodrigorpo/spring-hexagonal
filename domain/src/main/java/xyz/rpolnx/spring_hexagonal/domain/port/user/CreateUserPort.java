package xyz.rpolnx.spring_hexagonal.domain.port.user;

import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

public interface CreateUserPort {
    User create(User user);
}
