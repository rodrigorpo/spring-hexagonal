package xyz.rpolnx.spring_hexagonal.application.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import xyz.rpolnx.spring_hexagonal.application.model.UserDTO;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.CreateUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.DeleteUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetAllUsersUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetSingleUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.UpdateUserUseCase;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUser;
    private final DeleteUserUseCase deleteUser;
    private final GetAllUsersUseCase getAllUsers;
    private final GetSingleUserUseCase getSingleUser;
    private final UpdateUserUseCase updateUser;

    @GetMapping
    @ResponseStatus(OK)
    public List<UserDTO> getAll() {
        List<User> users = getAllUsers.getAll();
        return users.stream().map(UserDTO::fromUser).collect(toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserDTO getSingle(@PathVariable UUID id) {
        User user = getSingleUser.get(id);
        return UserDTO.fromUser(user);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO create(@RequestBody UserDTO userDTO) {
        User user = createUser.create(userDTO.toUser());
        return UserDTO.fromUser(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@RequestBody UserDTO userDTO, @PathVariable UUID id) {
        updateUser.update(userDTO.toUser(), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteUser.delete(id);
    }
}
