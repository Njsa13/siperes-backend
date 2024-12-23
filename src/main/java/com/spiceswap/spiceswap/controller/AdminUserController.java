package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.response.StatusResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.UserService;
import com.spiceswap.spiceswap.dto.response.AdminUserResponse;
import com.spiceswap.spiceswap.dto.response.UserInformation;
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

import static com.spiceswap.spiceswap.common.util.Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ADMIN_MANAGE_USER_PATS , produces = "application/json")
@Tag(name = "Manage User For Admin", description = "Manage User For Admin API")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @Schema(name = "GetAllUserRequest", description = "Get All User request body")
    @Operation(summary = "Endpoint for loading and searching for users in the admin section")
    public ResponseEntity<APIResultResponse<Page<AdminUserResponse>>> getAllUser(@RequestParam(value = "username", required = false) String username,
                                                                                 @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<AdminUserResponse> responses = userService.getAllUserForAdmin(username, pageable);
        APIResultResponse<Page<AdminUserResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded user list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/status/{username}")
    @Schema(name = "ChangeStatusRequest", description = "Change Status request body")
    @Operation(summary = "Endpoint to change user status to admin")
    public ResponseEntity<APIResultResponse<StatusResponse>> changeUserStatus(@PathVariable String username) {
        StatusResponse response = userService.changeUserStatus(username);
        APIResultResponse<StatusResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully changed user status",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/info")
    @Schema(name = "GetUsersInformationRequest", description = "Get Users Information request body")
    @Operation(summary = "Endpoint to retrieve statistical information about users")
    public ResponseEntity<APIResultResponse<UserInformation>> getRecipesInformation() {
        UserInformation response = userService.getUserInfo();
        APIResultResponse<UserInformation> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded information about users",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
