package xyz.rpolnx.spring_hexagonal.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import lombok.SneakyThrows;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;
import xyz.rpolnx.spring_hexagonal.infrastructure.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserExternalAdapterTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserExternalAdapter adapter;


    @Test
    @DisplayName("When there is user on database, should return them limiting by page")
    public void getUsersWhenDatabaseIsNotEmpty() {
        ReflectionTestUtils.setField(adapter, "PAGE_SIZE", 25);

        List<UUID> items = getUuidList(3);
        List<UserEntity> userEntities = generateUserEntityById(items, UserEntity.class);
        PageImpl<UserEntity> page = new PageImpl<>(userEntities);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        List<User> actual = adapter.getAll();
        List<User> expected = generateUserEntityById(items, User.class);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When there aren't users on database, should return empty list")
    public void getUsersWhenDatabaseOrPageIsEmpty() {
        ReflectionTestUtils.setField(adapter, "PAGE_SIZE", 50);

        List<UserEntity> userEntities = new ArrayList<>();
        PageImpl<UserEntity> page = new PageImpl<>(userEntities);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        List<User> actual = adapter.getAll();
        List<User> expected = new ArrayList<>();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When there are more users than page's size, then return only the page's size")
    public void shouldGetUsersSizeOfPassedPage() {
        ReflectionTestUtils.setField(adapter, "PAGE_SIZE", 15);

        List<UUID> items = getUuidList(25);
        List<UserEntity> userEntities = generateUserEntityById(items, UserEntity.class);
        PageImpl<UserEntity> page = new PageImpl<>(userEntities, PageRequest.of(0, 15), 25);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        List<User> actual = adapter.getAll();

        PagedListHolder<User> abstraction = new PagedListHolder<>(actual);
        abstraction.setPage(0);
        abstraction.setPageSize(15);

        assertEquals(15, abstraction.getPageList().size());
        assertEquals(2, abstraction.getPageCount());
    }

    @Test
    @DisplayName("When found user, should return it")
    public void getSingleUser() {
        UUID id = UUID.randomUUID();
        Optional<UserEntity> passed = Optional.of(new UserEntity(id, "Single GET"));

        when(repository.findById(id)).thenReturn(passed);

        Optional<User> actual = adapter.get(id);
        Optional<User> expected = Optional.of(new User(id, "Single GET"));

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When there is no user, should thrown exception")
    public void throwExceptionSingleUser() {
        UUID id = UUID.randomUUID();
        Optional<UserEntity> passed = Optional.empty();

        when(repository.findById(id)).thenReturn(passed);

        Optional<User> actual = adapter.get(id);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("When request to create user, should return the created entity")
    public void shouldCreatedUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "UPDATED USER");

        adapter.create(user);

        UserEntity expected = new UserEntity(id, "UPDATED USER");

        verify(repository, times(1)).save(expected);
    }

    @Test
    @DisplayName("When request to update user, should call repository and do nothing more")
    public void shouldUpdateUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "UPDATED USER");

        adapter.update(user, id);

        UserEntity expected = new UserEntity(id, "UPDATED USER");

        verify(repository, times(1)).save(expected);
    }

    @Test
    @DisplayName("When request to delete user, should call repository and do nothing more")
    public void shouldDeleteUser() {
        UUID id = UUID.randomUUID();

        adapter.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    private List<UUID> getUuidList(int size) {
        List<UUID> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            items.add(UUID.randomUUID());
        }

        return items;
    }

    @SneakyThrows
    private <T> List<T> generateUserEntityById(List<UUID> items, Class<T> clazz) {
        List<T> users = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            T instance = clazz
                    .getConstructor(UUID.class, String.class)
                    .newInstance(items.get(i), "User: #" + i);

            users.add(instance);
        }

        return users;
    }
}