package xyz.rpolnx.spring_hexagonal.domain.port.user;

import java.util.List;
import java.util.Optional;

import xyz.rpolnx.spring_hexagonal.domain.model.User;

public interface GetAllUsersPort {
    List<Optional<User>> getAll();
}
