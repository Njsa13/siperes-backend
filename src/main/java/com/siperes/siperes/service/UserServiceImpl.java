package com.siperes.siperes.service;

import com.siperes.siperes.common.util.CheckDataUtil;
import com.siperes.siperes.common.util.ImageUtil;
import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.dto.request.UpdateProfileImageRequest;
import com.siperes.siperes.dto.request.UpdateUserDetailRequest;
import com.siperes.siperes.dto.response.UpdateProfileImageResponse;
import com.siperes.siperes.dto.response.UpdateUserDetailResponse;
import com.siperes.siperes.dto.response.UserDetailResponse;
import com.siperes.siperes.exception.DataConflictException;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.User;
import com.siperes.siperes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail() {
        try {
            User user = jwtUtil.getUser();
            return UserDetailResponse.builder()
                    .username(user.getUsername())
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
            user.setUsername(request.getUsername());
            user.setName(request.getName());
            user.setDateOfBirth(request.getDateOfBirth().atStartOfDay());
            user = userRepository.save(user);
            return UpdateUserDetailResponse.builder()
                    .username(user.getUsername())
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
}
