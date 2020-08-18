package xyz.rpolnx.spring_hexagonal.domain.usecase.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete(UUID id);
}
