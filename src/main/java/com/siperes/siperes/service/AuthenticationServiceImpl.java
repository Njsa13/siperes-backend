package com.siperes.siperes.service;

import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.dto.request.LoginRequest;
import com.siperes.siperes.dto.request.RegisterRequest;
import com.siperes.siperes.dto.response.LoginResponse;
import com.siperes.siperes.dto.response.RefreshTokenResponse;
import com.siperes.siperes.dto.response.RegisterResponse;
import com.siperes.siperes.enumeration.*;
import com.siperes.siperes.exception.*;
import com.siperes.siperes.model.Token;
import com.siperes.siperes.model.User;
import com.siperes.siperes.repository.TokenRepository;
import com.siperes.siperes.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .name(request.getName())
                    .email(request.getEmail())
                    .isVerifiedEmail(false)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(EnumRole.USER)
                    .status(EnumStatus.ACTIVE)
                    .build();
            user = userRepository.save(user);
            emailVerificationService.sendEmail(request.getEmail(), EnumEmailVerificationType.REGISTER);
            return RegisterResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        } catch (Exception e) {
            log.error("Register Failed: {}", e.getMessage());
            throw new ServiceBusinessException(REGISTER_FAILED);
        }
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            User user;
            if (request.getCredential().contains("@")) {
                user = userRepository.findFirstByEmail(request.getCredential()).orElseThrow(
                        () -> new DataNotFoundException(EMAIL_NOT_FOUND)
                );
            } else {
                user = userRepository.findFirstByUsername(request.getCredential()).orElseThrow(
                        () -> new DataNotFoundException(USERNAME_NOT_FOUND)
                );
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    request.getPassword()
            ));
            if (!user.getIsVerifiedEmail()) {
                throw new NotVerifiedException(EMAIL_NOT_VERIFIED);
            }
            if (user.getStatus().equals(EnumStatus.INACTIVE)) {
                throw new UserNotActiveException(INACTIVE_USER);
            }

            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            saveToken(accessToken, user, EnumTokenAccessType.ACCESS);
            saveToken(refreshToken, user, EnumTokenAccessType.REFRESH);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            log.info(e.getMessage());
            throw new  BadCredentialsException(WRONG_PASSWORD);
        } catch (DataNotFoundException | UserNotActiveException | NotVerifiedException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Login Failed: {}",e.getMessage());
            throw new ServiceBusinessException(LOGIN_FAILED);
        }
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken() {
        try {
            String refreshToken = jwtUtil.getTokenFromRequest();
            String email = jwtUtil.extractEmail(refreshToken);
            User user = userRepository.findFirstByEmail(email)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            if (jwtUtil.isTokenValid(refreshToken, user)) {
                Token token = tokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new DataNotFoundException(TOKEN_NOT_FOUND));
                if (!token.getTokenAccessType().equals(EnumTokenAccessType.REFRESH)) {
                    throw new ForbiddenException(TOKEN_TYPE_INVALID);
                }
            } else {
                throw new ForbiddenException(INVALID_TOKEN);
            }
            String accessToken = jwtUtil.generateToken(user);
            saveToken(accessToken, user, EnumTokenAccessType.ACCESS);
            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (DataNotFoundException | ForbiddenException | JwtException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Refresh Token Failed: {}", e.getMessage());
            throw new ServiceBusinessException(REFRESH_TOKEN_FAILED);
        }
    }

    private void saveToken(String jwtToken, User user, EnumTokenAccessType tokenAccessType) {
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(EnumTokenType.BEARER)
                .tokenAccessType(tokenAccessType)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }
}
