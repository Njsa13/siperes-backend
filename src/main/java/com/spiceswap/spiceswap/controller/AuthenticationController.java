package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.*;
import com.spiceswap.spiceswap.dto.response.LoginResponse;
import com.spiceswap.spiceswap.dto.response.RefreshTokenResponse;
import com.spiceswap.spiceswap.dto.response.RegisterResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.enumeration.EnumEmailVerificationType;
import com.spiceswap.spiceswap.service.AuthenticationService;
import com.spiceswap.spiceswap.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import static com.spiceswap.spiceswap.common.util.Constants.AuthPats.AUTH_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_PATS, produces = "application/json")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailVerificationService emailVerificationService;
    private final LogoutHandler logoutHandler;

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
    @Operation(summary = "Endpoint untuk login, credential dapat berupa username/email")
    public ResponseEntity<APIResultResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.login(request);
        APIResultResponse<LoginResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Login berhasil",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/email-verify-register")
    @Schema(name = "VerifyEmailRequest", description = "Verify email request body")
    @Operation(summary = "Endpoint untuk verifikasi email")
    public ResponseEntity<APIResponse> verifyEmailRegister(@RequestParam String token) {
        emailVerificationService.verifyEmailRegister(token);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email berhasil diverifikasi"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/send-email-verify-register")
    @Schema(name = "SendVerifyEmailRequest", description = "Send verify email request body")
    @Operation(summary = "Endpoint untuk mengirim email berisi url untuk verifikasi, credential dapat berupa username/email")
    public ResponseEntity<APIResponse> sendVerificationEmailRegister(@RequestBody @Valid SendEmailVerificationRequest request) {
        emailVerificationService.sendEmail(request.getCredential(), EnumEmailVerificationType.REGISTER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email verifikasi berhasil dikirim"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resend-email-verify-register")
    @Schema(name = "ResendVerifyEmailRequest", description = "Resend verify email request body")
    @Operation(summary = "Endpoint untuk mengirim ulang email berisi url untuk verifikasi")
    public ResponseEntity<APIResponse> resendVerificationEmailRegister(@RequestBody @Valid ResendEmailVerificationRequest request) {
        emailVerificationService.resendVerificationEmailRegister(request.getToken(), EnumEmailVerificationType.REGISTER);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email verifikasi berhasil dikirim ulang"
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

    @PostMapping("/logout")
    @Schema(name = "LogoutRequest", description = "Logout request body")
    @Operation(summary = "Endpoint untuk logout, catatan: header juga dihapus ketika logout")
    public ResponseEntity<APIResponse> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutHandler.logout(request, response, authentication);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Logout berhasil"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/send-email-forgot-password")
    @Schema(name = "ForgotPasswordEmailRequest", description = "Forgot password email request body")
    @Operation(summary = "Endpoint untuk lupa password")
    public ResponseEntity<APIResponse> forgotPasswordEmail(@RequestBody @Valid ForgotPasswordRequest request) {
        emailVerificationService.sendEmail(request.getEmail(), EnumEmailVerificationType.FORGOT_PASSWORD);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Email ganti password berhasil dikirim"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/email-verify-forgot-password")
    @Schema(name = "VerifyForgotPasswordEmailRequest", description = "Verify forgot password email request body")
    @Operation(summary = "Endpoint untuk verifikasi ganti password ketika lupa password")
    public ResponseEntity<APIResponse> verifyForgotPasswordEmail(@RequestBody @Valid ResetPasswordRequest request) {
        emailVerificationService.verifyEmailTokenForgotPassword(request);
        APIResponse response = new APIResponse(
                HttpStatus.OK,
                "Password berhasil direset"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
