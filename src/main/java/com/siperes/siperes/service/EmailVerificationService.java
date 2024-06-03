package com.siperes.siperes.service;

import com.siperes.siperes.enumeration.EnumEmailVerificationType;

public interface EmailVerificationService {
    void sendEmail(String credential, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
}
