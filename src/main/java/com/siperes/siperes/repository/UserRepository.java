package com.siperes.siperes.repository;

import com.siperes.siperes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByEmail(String email);
    Optional<User> findFirstByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
