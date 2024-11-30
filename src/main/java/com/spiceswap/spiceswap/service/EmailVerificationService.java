package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.ResetPasswordRequest;
import com.spiceswap.spiceswap.enumeration.EnumEmailVerificationType;

public interface EmailVerificationService {
    void sendEmail(String credential, EnumEmailVerificationType emailVerificationType);
    void resendVerificationEmailRegister(String token, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
    void verifyEmailTokenForgotPassword(ResetPasswordRequest request);
}
