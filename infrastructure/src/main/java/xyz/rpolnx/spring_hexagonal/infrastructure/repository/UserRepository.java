package xyz.rpolnx.spring_hexagonal.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {

    @Override
    Page<UserEntity> findAll(Pageable pageable);
}
