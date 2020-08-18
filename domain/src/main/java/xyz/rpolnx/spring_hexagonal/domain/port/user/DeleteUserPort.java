package xyz.rpolnx.spring_hexagonal.domain.port.user;

import java.util.UUID;

public interface DeleteUserPort {
    void delete(UUID id);
}
