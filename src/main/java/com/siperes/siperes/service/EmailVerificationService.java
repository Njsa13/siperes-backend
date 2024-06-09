package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.ResendEmailVerificationRequest;
import com.siperes.siperes.enumeration.EnumEmailVerificationType;

public interface EmailVerificationService {
    void sendEmail(String credential, EnumEmailVerificationType emailVerificationType);
    void resendVerificationEmailRegister(String token, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
}
