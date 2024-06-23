package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.ChangePasswordRequest;
import com.siperes.siperes.dto.request.UpdateProfileImageRequest;
import com.siperes.siperes.dto.request.UpdateUserDetailRequest;
import com.siperes.siperes.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDetailResponse getUserDetail();
    UpdateUserDetailResponse updateUserDetail(UpdateUserDetailRequest request);
    UpdateProfileImageResponse updateProfileImage(UpdateProfileImageRequest request);
    void deleteProfileImage();
    void changePassword(ChangePasswordRequest request);
    UserResponse getOtherUserProfile(String username);
    Page<AdminUserResponse> getAllUserForAdmin(String username, Pageable pageable);
    StatusResponse changeUserStatus(String username);
    UserInformation getUserInfo();
}
