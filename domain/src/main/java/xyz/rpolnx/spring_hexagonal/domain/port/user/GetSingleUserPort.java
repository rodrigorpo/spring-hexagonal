package xyz.rpolnx.spring_hexagonal.domain.port.user;

import java.util.Optional;
import java.util.UUID;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface GetSingleUserPort {
    Optional<User> get(UUID uuid);
}
