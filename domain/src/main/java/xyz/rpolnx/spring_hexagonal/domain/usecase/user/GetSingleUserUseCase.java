package xyz.rpolnx.spring_hexagonal.domain.usecase.user;

import java.util.UUID;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface GetSingleUserUseCase {
    User get(UUID uuid);
}
