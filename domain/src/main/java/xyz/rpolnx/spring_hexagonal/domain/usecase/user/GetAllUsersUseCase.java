package xyz.rpolnx.spring_hexagonal.domain.usecase.user;

import java.util.List;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface GetAllUsersUseCase {
    List<User> getAll();
}
