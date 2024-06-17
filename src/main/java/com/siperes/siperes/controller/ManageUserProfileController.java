package com.siperes.siperes.controller;

import com.siperes.siperes.dto.request.ChangePasswordRequest;
import com.siperes.siperes.dto.request.UpdateProfileImageRequest;
import com.siperes.siperes.dto.request.UpdateUserDetailRequest;
import com.siperes.siperes.dto.response.UpdateProfileImageResponse;
import com.siperes.siperes.dto.response.UpdateUserDetailResponse;
import com.siperes.siperes.dto.response.UserDetailResponse;
import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.siperes.siperes.common.util.Constants.ManageUserProfile.MANAGE_USER_PROFILE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = MANAGE_USER_PROFILE_PATS, produces = "application/json")
@Tag(name = "Manage User Profile", description = "Manage User Profile API")
public class ManageUserProfileController {
    private final UserService userService;

    @GetMapping
    @Schema(name = "GetUserDetailRequest", description = "Get User Detail request body")
    @Operation(summary = "Endpoint untuk menampilkan data user")
    public ResponseEntity<APIResultResponse<UserDetailResponse>> getUserDetail() {
        UserDetailResponse responses = userService.getUserDetail();
        APIResultResponse<UserDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat data user",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    @Schema(name = "UpdateUserRequest", description = "Update User request body")
    @Operation(summary = "Endpoint untuk memperbarui data user")
    public ResponseEntity<APIResultResponse<UpdateUserDetailResponse>> updateUser(@RequestBody @Valid UpdateUserDetailRequest request) {
        UpdateUserDetailResponse response = userService.updateUserDetail(request);
        APIResultResponse<UpdateUserDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memperbarui data user",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/update-profile-image")
    @Schema(name = "UpdateProfileImageRequest", description = "Update Profile Image request body")
    @Operation(summary = "Endpoint untuk memperbarui foto profile")
    public ResponseEntity<APIResultResponse<UpdateProfileImageResponse>> updateProfileImage(@RequestBody @Valid UpdateProfileImageRequest request) {
        UpdateProfileImageResponse response = userService.updateProfileImage(request);
        APIResultResponse<UpdateProfileImageResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memperbarui foto profil",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-profile-image")
    @Schema(name = "DeleteProfileImageRequest", description = "Delete Profile Image request body")
    @Operation(summary = "Endpoint untuk menghapus foto profile")
    public ResponseEntity<APIResponse> deleteProfileImage() {
        userService.deleteProfileImage();
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menghapus foto profile"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @Schema(name = "ChangePasswordRequest", description = "Change password request body")
    @Operation(summary = "Endpoint untuk mengganti password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Password berhasil diperbarui"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
