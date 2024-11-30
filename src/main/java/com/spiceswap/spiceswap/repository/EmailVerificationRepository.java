package com.spiceswap.spiceswap.repository;

import com.spiceswap.spiceswap.enumeration.EnumEmailVerificationType;
import com.spiceswap.spiceswap.model.EmailVerification;
import com.spiceswap.spiceswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findFirstByUserAndEmailVerificationTypeOrderByExpTimeDesc(User user, EnumEmailVerificationType emailVerificationType);
    Optional<EmailVerification> findFirstByToken(String token);
    Boolean existsByToken(String token);
    Optional<EmailVerification> findFirstByTokenAndEmailVerificationType(String token, EnumEmailVerificationType type);
}