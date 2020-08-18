package xyz.rpolnx.spring_hexagonal.domain.port.user;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface CreateUserPort {
    User create(User user);
}
