package xyz.rpolnx.spring_hexagonal.domain.port.user;

import java.util.UUID;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface UpdateUserPort {
    void update(User user, UUID id);
}
