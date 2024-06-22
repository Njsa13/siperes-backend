package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.ChangePasswordRequest;
import com.siperes.siperes.dto.request.UpdateProfileImageRequest;
import com.siperes.siperes.dto.request.UpdateUserDetailRequest;
import com.siperes.siperes.dto.response.UpdateProfileImageResponse;
import com.siperes.siperes.dto.response.UpdateUserDetailResponse;
import com.siperes.siperes.dto.response.UserDetailResponse;
import com.siperes.siperes.dto.response.UserResponse;

public interface UserService {
    UserDetailResponse getUserDetail();
    UpdateUserDetailResponse updateUserDetail(UpdateUserDetailRequest request);
    UpdateProfileImageResponse updateProfileImage(UpdateProfileImageRequest request);
    void deleteProfileImage();
    void changePassword(ChangePasswordRequest request);
    UserResponse getOtherUserProfile(String username);
}
