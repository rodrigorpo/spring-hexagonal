package xyz.rpolnx.spring_hexagonal.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import xyz.rpolnx.spring_hexagonal.infrastructure.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {

    @Override
    Page<UserEntity> findAll(Pageable pageable);
}
