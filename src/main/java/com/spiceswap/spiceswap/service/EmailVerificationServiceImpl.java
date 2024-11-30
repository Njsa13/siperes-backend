package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.common.util.MailUtil;
import com.spiceswap.spiceswap.dto.request.ResetPasswordRequest;
import com.spiceswap.spiceswap.enumeration.EnumEmailVerificationType;
import com.spiceswap.spiceswap.exception.DataNotFoundException;
import com.spiceswap.spiceswap.exception.ForbiddenException;
import com.spiceswap.spiceswap.exception.ServiceBusinessException;
import com.spiceswap.spiceswap.model.EmailVerification;
import com.spiceswap.spiceswap.model.User;
import com.spiceswap.spiceswap.repository.EmailVerificationRepository;
import com.spiceswap.spiceswap.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.spiceswap.spiceswap.common.util.Constants.ErrorMessage.*;
import static com.spiceswap.spiceswap.common.util.Constants.EmailMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;

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
                user = userRepository.findFirstByUserName(credential).orElseThrow(
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
    public void resendVerificationEmailRegister(String token, EnumEmailVerificationType emailVerificationType) {
        try {
            EmailVerification emailVerification = emailVerificationRepository.findFirstByToken(token)
                    .orElseThrow(() -> new DataNotFoundException(REGISTER_TOKEN_NOT_FOUND));
            sendEmail(emailVerification.getUser().getEmail(), emailVerificationType);
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Resend Email Failed: {}", e.getMessage());
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
            log.error("Verify Email Failed: {}", e.getMessage());
            throw new ServiceBusinessException(VERIFY_EMAIL_FAILED);
        }
    }

    @Override
    @Transactional
    public void verifyEmailTokenForgotPassword(ResetPasswordRequest request) {
        try {
            emailVerificationRepository.findFirstByTokenAndEmailVerificationType(request.getToken(), EnumEmailVerificationType.FORGOT_PASSWORD).ifPresentOrElse(emailVerification -> {
                if (emailVerification.getExpTime().isBefore(LocalDateTime.now())) {
                    throw new ForbiddenException(TOKEN_EXPIRED);
                }
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    throw new ValidationException(INVALID_CONFIRM_PASSWORD);
                }
                User user = emailVerification.getUser();
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
                emailVerification.setUser(null);
                emailVerificationRepository.delete(emailVerification);
            }, () -> {
                throw new DataNotFoundException(TOKEN_NOT_FOUND);
            });
        } catch (DataNotFoundException | ForbiddenException | ValidationException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_RESET_PASSWORD);
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
