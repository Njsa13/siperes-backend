package com.siperes.siperes.repository;

import com.siperes.siperes.enumeration.EnumRole;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByEmail(String email);
    Optional<User> findFirstByUserName(String username);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM User u
            JOIN u.bookmarks r
            WHERE u.id = :userId AND r.id = :recipeId
            """)
    boolean existsBookmarkByUserIdAndRecipeId(@Param("userId") UUID userId, @Param("recipeId") UUID recipeId);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Query("""
            SELECT COUNT(u) FROM User u
            WHERE u.role = :role
            """)
    long countUsersByRole(EnumRole role);

    @Query("""
            SELECT COUNT(u) FROM User u
            WHERE u.role = :role And u.status = :status
            """)
    long countUsersByRoleAndStatus(EnumRole role, EnumStatus status);

    @Query("""
            SELECT COUNT(u) FROM User u
            WHERE u.lastLogin >= :startDate AND u.role = :role
            """)
    long countUserLoginLast(EnumRole role, LocalDateTime startDate);
}
