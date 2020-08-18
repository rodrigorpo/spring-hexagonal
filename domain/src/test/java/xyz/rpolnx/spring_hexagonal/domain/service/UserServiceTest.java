package xyz.rpolnx.spring_hexagonal.domain.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.rpolnx.spring_hexagonal.domain.exception.NotFoundException;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.domain.port.user.CreateUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.DeleteUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetAllUsersPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetSingleUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.UpdateUserPort;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private GetAllUsersPort getAllUsers;
    @Mock
    private GetSingleUserPort getSingleUser;
    @Mock
    private CreateUserPort createUser;
    @Mock
    private UpdateUserPort updateUser;
    @Mock
    private DeleteUserPort deleteUser;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("When there are any users, should return a non empty list")
    public void shouldReturnedFilledUserList() {
        List<User> expected = generateUserList();
        when(getAllUsers.getAll())
                .thenReturn(expected);

        List<User> actual = userService.getAll();

        assertFalse(actual.isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When there aren't users on database, should return a empty list")
    public void shouldReturnedEmptyUserList() {
        when(getAllUsers.getAll())
                .thenReturn(emptyList());

        List<User> actual = userService.getAll();

        assertTrue(actual.isEmpty());
        assertEquals(emptyList(), actual);
    }

    @Test
    @DisplayName("When user exists on database, should return it")
    public void shouldReturnedSingleUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "User 2");
        when(getSingleUser.get(id))
                .thenReturn(Optional.ofNullable(user));

        User actual = userService.get(id);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("When user not exists on database, should throw exception")
    public void shouldThrowErrorFindingSingleUser() {
        UUID id = UUID.randomUUID();
        when(getSingleUser.get(id))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.get(id));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("When creating user, should return created id")
    public void shouldCreateUser() {
        User requestUser = new User(null, "Created user");
        User user = requestUser.withGenerateId();

        when(createUser.create(requestUser))
                .thenReturn(user);

        User actual = userService.create(requestUser);

        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("When updating user, should return created id")
    public void shouldUpdateUser() {
        User requestUser = new User(UUID.randomUUID(), "Updated user");
        UUID pathId = UUID.randomUUID();
        userService.update(requestUser, pathId);

        Mockito.verify(updateUser, times(1)).update(requestUser, pathId);
    }

    @Test
    @DisplayName("When deleting an existing user, should throw exception")
    public void shouldDeleteExistingUser() {
        UUID pathId = UUID.randomUUID();

        userService.delete(pathId);

        Mockito.verify(deleteUser, times(1)).delete(pathId);
    }

    @Test
    @DisplayName("When deleting an non existing user, should return created id")
    public void shouldDeleteNonExistingUser() {
        UUID pathId = UUID.randomUUID();

        userService.delete(pathId);

        Mockito.verify(deleteUser, times(1)).delete(pathId);
    }

    private static List<User> generateUserList() {
        return List.of(new User(UUID.randomUUID(), "User 1"),
                new User(UUID.randomUUID(), "User 2"));
    }
}