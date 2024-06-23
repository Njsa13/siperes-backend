package com.siperes.siperes.service;

import com.siperes.siperes.common.util.CheckDataUtil;
import com.siperes.siperes.common.util.ImageUtil;
import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.dto.request.ChangePasswordRequest;
import com.siperes.siperes.dto.request.UpdateProfileImageRequest;
import com.siperes.siperes.dto.request.UpdateUserDetailRequest;
import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.enumeration.EnumRole;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.exception.DataConflictException;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.User;
import com.siperes.siperes.repository.UserRepository;
import com.siperes.siperes.repository.specification.UserSpecification;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CheckDataUtil checkDataUtil;
    private final ImageUtil imageUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail() {
        try {
            User user = jwtUtil.getUser();
            return UserDetailResponse.builder()
                    .username(user.getUserName())
                    .name(user.getName())
                    .email(user.getEmail())
                    .isEmailVerified(user.getIsVerifiedEmail())
                    .role(user.getRole())
                    .dateOfBirth(Optional.ofNullable(user.getDateOfBirth())
                            .map(LocalDateTime::toLocalDate)
                            .orElse(null))
                    .profileImageLink(user.getProfileImageLink())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_USER_DETAIL);
        }
    }

    @Override
    @Transactional
    public UpdateUserDetailResponse updateUserDetail(UpdateUserDetailRequest request) {
        try {
            User user = jwtUtil.getUser();
            checkDataUtil.checkDataField("users", "username", request.getUsername(), "user_id", user.getId());
            user.setUserName(request.getUsername());
            user.setName(request.getName());
            user.setDateOfBirth(request.getDateOfBirth().atStartOfDay());
            user = userRepository.save(user);
            return UpdateUserDetailResponse.builder()
                    .username(user.getUserName())
                    .name(user.getName())
                    .dateOfBirth(Optional.ofNullable(user.getDateOfBirth())
                            .map(LocalDateTime::toLocalDate)
                            .orElse(null))
                    .updatedAt(user.getUpdateAt())
                    .build();
        } catch (DataNotFoundException | DataConflictException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_UPDATE_USER_DETAIL);
        }
    }

    @Override
    @Transactional
    public UpdateProfileImageResponse updateProfileImage(UpdateProfileImageRequest request) {
        try {
            User user = jwtUtil.getUser();
            Optional.ofNullable(user.getProfileImageLink())
                    .ifPresent(imageUtil::deleteImage);
            String thumbnailImageLink = imageUtil.base64UploadImage(request.getProfileImage()).join();
            user.setProfileImageLink(thumbnailImageLink);
            user = userRepository.save(user);
            return UpdateProfileImageResponse.builder()
                    .profileImageLink(user.getProfileImageLink())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_UPDATE_PROFILE_IMAGE);
        }
    }

    @Override
    @Transactional
    public void deleteProfileImage() {
        try {
            User user = jwtUtil.getUser();
            Optional.ofNullable(user.getProfileImageLink())
                    .ifPresentOrElse(imageLink -> {
                        imageUtil.deleteImage(imageLink);
                        user.setProfileImageLink(null);
                        userRepository.save(user);
                    }, () -> {
                        throw new DataNotFoundException(IMAGE_PROFILE_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_DELETE_PROFILE_IMAGE);
        }
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        try {
            User user = jwtUtil.getUser();
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new ValidationException(INVALID_CURRENT_PASSWORD);
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new ValidationException(INVALID_CONFIRM_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } catch (ValidationException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_CHANGE_PASSWORD);
        }
    }

    @Override
    public UserResponse getOtherUserProfile(String username) {
        try {
            User user = userRepository.findFirstByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            return UserResponse.builder()
                    .username(user.getUserName())
                    .name(user.getName())
                    .profileImageLink(user.getProfileImageLink())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_OTHER_USER_PROFILE);
        }
    }

    @Override
    public Page<AdminUserResponse> getAllUserForAdmin(String username, Pageable pageable) {
        try {
            Specification<User> spec = UserSpecification.hasUsernameAndRole(username, EnumRole.USER);
            Page<User> userPage = Optional.ofNullable(userRepository.findAll(spec, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            return userPage.map(user -> AdminUserResponse.builder()
                    .username(user.getUserName())
                    .name(user.getName())
                    .email(user.getEmail())
                    .isVerifiedEmail(user.getIsVerifiedEmail())
                    .dateOfBirth(user.getDateOfBirth())
                    .status(user.getStatus())
                    .lastLogin(user.getLastLogin())
                    .profileImageLink(user.getProfileImageLink())
                    .createdAt(user.getCreatedAt())
                    .updateAt(user.getUpdateAt())
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_DATA_USER);
        }
    }

    @Override
    public StatusResponse changeUserStatus(String username) {
        try {
            User user = userRepository.findFirstByUserName(username)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            if (user.getStatus().equals(EnumStatus.ACTIVE)) {
                user.setStatus(EnumStatus.INACTIVE);
            } else if (user.getStatus().equals(EnumStatus.INACTIVE)) {
                user.setStatus(EnumStatus.ACTIVE);
            }
            user = userRepository.save(user);
            return StatusResponse.builder()
                    .status(user.getStatus())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_CHANGE_USER_STATUS);
        }
    }

    @Override
    public UserInformation getUserInfo() {
        try {
            long totalUsers = userRepository.countUsersByRole(EnumRole.USER);
            long totalActiveUser = userRepository.countUsersByRoleAndStatus(EnumRole.USER, EnumStatus.ACTIVE);
            long totalInactiveUser = userRepository.countUsersByRoleAndStatus(EnumRole.USER, EnumStatus.INACTIVE);
            long daily = userRepository.countUserLoginLast(EnumRole.USER, LocalDateTime.now().minusDays(1));
            long weekly = userRepository.countUserLoginLast(EnumRole.USER, LocalDateTime.now().minusWeeks(1));
            long monthly = userRepository.countUserLoginLast(EnumRole.USER, LocalDateTime.now().minusMonths(1));
            return UserInformation.builder()
                    .totalUser((int) totalUsers)
                    .totalActiveUser((int) totalActiveUser)
                    .totalInactiveUser((int) totalInactiveUser)
                    .loginActiveUser(LoginActiveUser.builder()
                            .daily((int) daily)
                            .weekly((int) weekly)
                            .monthly((int) monthly)
                            .build())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_USER_INFO);
        }
    }
}
