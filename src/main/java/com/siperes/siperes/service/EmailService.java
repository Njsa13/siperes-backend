package com.siperes.siperes.service;

import com.siperes.siperes.enumeration.EnumEmailVerificationType;

public interface EmailService {
    void sendEmail(String email, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
}
