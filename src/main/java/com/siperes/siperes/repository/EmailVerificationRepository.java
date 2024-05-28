package com.siperes.siperes.repository;

import com.siperes.siperes.enumeration.EnumEmailVerificationType;
import com.siperes.siperes.model.EmailVerification;
import com.siperes.siperes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findFirstByUserAndEmailVerificationTypeOrderByExpTimeDesc(User user, EnumEmailVerificationType emailVerificationType);
    Optional<EmailVerification> findFirstByToken(String token);
    Boolean existsByToken(String token);
}