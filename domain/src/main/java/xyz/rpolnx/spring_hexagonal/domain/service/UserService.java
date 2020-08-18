package xyz.rpolnx.spring_hexagonal.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.rpolnx.spring_hexagonal.domain.exception.NotFoundException;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.domain.port.user.CreateUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.DeleteUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetAllUsersPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetSingleUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.UpdateUserPort;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.CreateUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.DeleteUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetAllUsersUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetSingleUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.UpdateUserUseCase;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, DeleteUserUseCase, GetAllUsersUseCase, GetSingleUserUseCase,
        UpdateUserUseCase {

    private final GetAllUsersPort getAllUsers;
    private final GetSingleUserPort getSingleUser;
    private final CreateUserPort createUser;
    private final UpdateUserPort updateUser;
    private final DeleteUserPort deleteUser;


    @Override
    public List<User> getAll() {
        return getAllUsers.getAll();
    }

    @Override
    public User get(UUID uuid) {

        return getSingleUser.get(uuid)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    @Override
    public User create(User user) {
        return createUser.create(user);
    }

    @Override
    public void delete(UUID id) {
        deleteUser.delete(id);
    }

    @Override
    public void update(User user, UUID id) {
        updateUser.update(user, id);
    }
}
