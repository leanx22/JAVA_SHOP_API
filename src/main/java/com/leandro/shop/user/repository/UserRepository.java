package com.leandro.shop.user.repository;

import com.leandro.shop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Override
    boolean existsById(UUID uuid);

    boolean existsByEmail(String email);
}
