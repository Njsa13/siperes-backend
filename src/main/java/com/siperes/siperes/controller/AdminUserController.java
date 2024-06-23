package com.siperes.siperes.controller;

import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.siperes.siperes.common.util.Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ADMIN_MANAGE_USER_PATS , produces = "application/json")
@Tag(name = "Manage User For Admin", description = "Manage User For Admin API")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @Schema(name = "GetAllUserRequest", description = "Get All User request body")
    @Operation(summary = "Endpoint untuk memuat dan mencari user pada bagian admin")
    public ResponseEntity<APIResultResponse<Page<AdminUserResponse>>> getAllUser(@RequestParam(value = "username", required = false) String username,
                                                                                 @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<AdminUserResponse> responses = userService.getAllUserForAdmin(username, pageable);
        APIResultResponse<Page<AdminUserResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar user",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/status/{username}")
    @Schema(name = "ChangeStatusRequest", description = "Change Status request body")
    @Operation(summary = "Endpoint untuk mengubah status user untuk admin")
    public ResponseEntity<APIResultResponse<StatusResponse>> changeUserStatus(@PathVariable String username) {
        StatusResponse response = userService.changeUserStatus(username);
        APIResultResponse<StatusResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil mengganti status user",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/info")
    @Schema(name = "GetUsersInformationRequest", description = "Get Users Information request body")
    @Operation(summary = "Endpoint untuk mengambil informasi statistik tentang user")
    public ResponseEntity<APIResultResponse<UserInformation>> getRecipesInformation() {
        UserInformation response = userService.getUserInfo();
        APIResultResponse<UserInformation> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat informasi tentang user",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
