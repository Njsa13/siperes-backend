package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.ResetPasswordRequest;
import com.siperes.siperes.enumeration.EnumEmailVerificationType;

public interface EmailVerificationService {
    void sendEmail(String credential, EnumEmailVerificationType emailVerificationType);
    void resendVerificationEmailRegister(String token, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
    void verifyEmailTokenForgotPassword(ResetPasswordRequest request);
}
