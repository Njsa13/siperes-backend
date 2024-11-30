package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.ChangePasswordRequest;
import com.spiceswap.spiceswap.dto.request.UpdateProfileImageRequest;
import com.spiceswap.spiceswap.dto.request.UpdateUserDetailRequest;
import com.spiceswap.spiceswap.dto.response.UpdateProfileImageResponse;
import com.spiceswap.spiceswap.dto.response.UpdateUserDetailResponse;
import com.spiceswap.spiceswap.dto.response.UserDetailResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.UserService;
import com.spiceswap.spiceswap.common.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.ManageUserProfile.MANAGE_USER_PROFILE_PATS, produces = "application/json")
@Tag(name = "Manage User Profile", description = "Manage User Profile API")
public class ManageUserProfileController {
    private final UserService userService;

    @GetMapping
    @Schema(name = "GetUserDetailRequest", description = "Get User Detail request body")
    @Operation(summary = "Endpoint to load user data")
    public ResponseEntity<APIResultResponse<UserDetailResponse>> getUserDetail() {
        UserDetailResponse responses = userService.getUserDetail();
        APIResultResponse<UserDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded user data",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    @Schema(name = "UpdateUserRequest", description = "Update User request body")
    @Operation(summary = "Endpoint for updating user data")
    public ResponseEntity<APIResultResponse<UpdateUserDetailResponse>> updateUser(@RequestBody @Valid UpdateUserDetailRequest request) {
        UpdateUserDetailResponse response = userService.updateUserDetail(request);
        APIResultResponse<UpdateUserDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully updated user data",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/update-profile-image")
    @Schema(name = "UpdateProfileImageRequest", description = "Update Profile Image request body")
    @Operation(summary = "Endpoint for updating profile picture")
    public ResponseEntity<APIResultResponse<UpdateProfileImageResponse>> updateProfileImage(@RequestBody @Valid UpdateProfileImageRequest request) {
        UpdateProfileImageResponse response = userService.updateProfileImage(request);
        APIResultResponse<UpdateProfileImageResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully updated profile picture",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-profile-picture")
    @Schema(name = "DeleteProfileImageRequest", description = "Delete Profile Image request body")
    @Operation(summary = "Endpoint for deleting profile picture")
    public ResponseEntity<APIResponse> deleteProfileImage() {
        userService.deleteProfileImage();
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully deleted profile picture"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @Schema(name = "ChangePasswordRequest", description = "Change password request body")
    @Operation(summary = "Endpoint to change password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Password updated successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
