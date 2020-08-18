package xyz.rpolnx.spring_hexagonal.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.domain.port.user.CreateUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.DeleteUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetAllUsersPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.GetSingleUserPort;
import xyz.rpolnx.spring_hexagonal.domain.port.user.UpdateUserPort;
import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;
import xyz.rpolnx.spring_hexagonal.infrastructure.repository.UserRepository;
import xyz.rpolnx.spring_hexagonal.infrastructure.util.UserFactory;

@Component
@RequiredArgsConstructor
public class UserExternalAdapter implements CreateUserPort, DeleteUserPort, GetAllUsersPort, GetSingleUserPort,
        UpdateUserPort {
    private final UserRepository repository;

    @Value("${user.page.size:50}")
    private int PAGE_SIZE;

    @Override
    public List<User> getAll() {
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        return repository.findAll(pageRequest)
                .map(UserFactory::fromEntity)
                .get()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> get(UUID uuid) {
        return repository.findById(uuid)
                .map(UserFactory::fromEntity);
    }

    @Override
    public User create(User user) {
        UserEntity entity = new UserEntity(user.getId(), user.getName());

        UserEntity created = repository.save(entity);

        return UserFactory.fromEntity(created);
    }

    @Override
    public void update(User user, UUID id) {
        UserEntity entity = new UserEntity(id, user.getName());

        repository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
