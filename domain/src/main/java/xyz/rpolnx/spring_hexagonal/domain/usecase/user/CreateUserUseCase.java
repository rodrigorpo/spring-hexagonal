package xyz.rpolnx.spring_hexagonal.domain.usecase.user;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface CreateUserUseCase {
    User create(User user);
}
