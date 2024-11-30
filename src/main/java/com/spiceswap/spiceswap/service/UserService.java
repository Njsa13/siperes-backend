package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.ChangePasswordRequest;
import com.spiceswap.spiceswap.dto.request.UpdateProfileImageRequest;
import com.spiceswap.spiceswap.dto.request.UpdateUserDetailRequest;
import com.spiceswap.spiceswap.dto.response.*;
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
