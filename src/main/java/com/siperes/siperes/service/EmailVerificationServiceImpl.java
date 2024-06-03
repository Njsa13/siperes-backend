package com.siperes.siperes.service;

import com.siperes.siperes.common.util.MailUtil;
import com.siperes.siperes.enumeration.EnumEmailVerificationType;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ForbiddenException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.EmailVerification;
import com.siperes.siperes.model.User;
import com.siperes.siperes.repository.EmailVerificationRepository;
import com.siperes.siperes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;
import static com.siperes.siperes.common.util.Constants.EmailMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailUtil mailUtil;

    @Override
    @Transactional
    public void sendEmail(String credential, EnumEmailVerificationType emailVerificationType) {
        try {
            User user;
            if (credential.contains("@")) {
                user = userRepository.findFirstByEmail(credential).orElseThrow(
                        () -> new DataNotFoundException(EMAIL_NOT_FOUND)
                );
            } else {
                user = userRepository.findFirstByUsername(credential).orElseThrow(
                        () -> new DataNotFoundException(USERNAME_NOT_FOUND)
                );
            }
            String verificationUrl = getVerificationUrl(emailVerificationType, user);
            String subject;
            String text;
            if (user.getIsVerifiedEmail()) {
                if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
                    throw new ForbiddenException(EMAIL_ALREADY_VERIFIED);
                } else {
                    subject = RESET_PASSWORD_SUBJECT;
                    text = RESET_PASSWORD_TEXT + verificationUrl;
                }
            } else {
                if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
                    subject = EMAIL_VERIFICATION_SUBJECT;
                    text = EMAIL_VERIFICATION_TEXT + verificationUrl;
                } else {
                    throw new ForbiddenException(EMAIL_NOT_VERIFIED);
                }
            }
            mailUtil.sendEmail(user.getEmail(), subject, text);
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Send Email Failed: {}", e.getMessage());
            throw new ServiceBusinessException(SEND_EMAIL_FAILED);
        }
    }

    @Override
    @Transactional
    public void verifyEmailRegister(String token) {
        try {
            emailVerificationRepository.findFirstByToken(token)
                    .ifPresentOrElse(val -> {
                        if (val.getExpTime().isBefore(LocalDateTime.now())) {
                            throw new ForbiddenException(TOKEN_EXPIRED);
                        }
                        User user = val.getUser();
                        if (user.getIsVerifiedEmail()) {
                            throw new ForbiddenException(EMAIL_ALREADY_VERIFIED);
                        }
                        user.setIsVerifiedEmail(true);
                        userRepository.save(user);
                    }, () -> {
                        throw new DataNotFoundException(REGISTER_TOKEN_NOT_FOUND);
                    });
        } catch (ForbiddenException | DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Verify Email Failed: {}", e.getMessage());
            throw new ServiceBusinessException(VERIFY_EMAIL_FAILED);
        }
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String getVerificationUrl(EnumEmailVerificationType emailVerificationType, User user) {
        LocalDateTime currentTime = LocalDateTime.now();
        String token = generateToken();
        while (emailVerificationRepository.existsByToken(token)) {
            token = generateToken();
        }
        emailVerificationRepository.findFirstByUserAndEmailVerificationTypeOrderByExpTimeDesc(user, emailVerificationType)
                .ifPresent(val -> {
                    if (val.getExpTime().isAfter(currentTime)) {
                        throw new ForbiddenException(TWO_MIN_COOL_DOWN);
                    }
                });
        EmailVerification emailVerification = EmailVerification.builder()
                .token(token)
                .emailVerificationType(emailVerificationType)
                .expTime(currentTime.plusMinutes(2))
                .user(user)
                .build();
        emailVerificationRepository.save(emailVerification);
        if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
            return EMAIL_VERIFICATION_URL + token;
        } else {
            return RESET_PASSWORD_URL + token;
        }
    }
}
