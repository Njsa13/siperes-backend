package com.siperes.siperes.controller;

import com.siperes.siperes.dto.request.LoginRequest;
import com.siperes.siperes.dto.request.RegisterRequest;
import com.siperes.siperes.dto.request.ResendEmailVerificationRequest;
import com.siperes.siperes.dto.response.LoginResponse;
import com.siperes.siperes.dto.response.RefreshTokenResponse;
import com.siperes.siperes.dto.response.RegisterResponse;
import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.enumeration.EnumEmailVerificationType;
import com.siperes.siperes.service.AuthenticationService;
import com.siperes.siperes.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.siperes.siperes.common.util.Constants.AuthPats.AUTH_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATS, produces = "application/json")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/register")
    @Schema(name = "RegisterRequest", description = "Register request body")
    @Operation(summary = "Endpoint untuk register akun baru")
    public ResponseEntity<APIResultResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authenticationService.register(request);
        APIResultResponse<RegisterResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Daftar berhasil",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Schema(name = "LoginRequest", description = "Login request body")
    @Operation(summary = "Endpoint untuk login")
    public ResponseEntity<APIResultResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.login(request);
        APIResultResponse<LoginResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Login berhasil",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/email-verify-register")
    @Schema(name = "VerifyEmailRequest", description = "Verify email request body")
    @Operation(summary = "Endpoint untuk verifikasi email")
    public ResponseEntity<APIResponse> verifyEmailRegister(@RequestParam String token) {
        emailService.verifyEmailRegister(token);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email berhasil diverifikasi"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resend-email-verify-register")
    @Schema(name = "ResendVerifyEmailRequest", description = "Resend verify email request body")
    @Operation(summary = "Endpoint untuk mengirim ulang email berisi url untuk verifikasi")
    public ResponseEntity<APIResponse> resendVerificationEmailRegister(@RequestBody @Valid ResendEmailVerificationRequest request) {
        emailService.sendEmail(request.getEmail(), EnumEmailVerificationType.REGISTER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email berhasil dikirim ulang"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    @Schema(name = "RefreshTokenRequest", description = "Refresh token request body")
    @Operation(summary = "Endpoint me-refresh token akses")
    public ResponseEntity<APIResultResponse<RefreshTokenResponse>> refreshToken() {
        RefreshTokenResponse response = authenticationService.refreshToken();
        APIResultResponse<RefreshTokenResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Refresh token berhasil",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }
}
