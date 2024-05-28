package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.LoginRequest;
import com.siperes.siperes.dto.request.RegisterRequest;
import com.siperes.siperes.dto.response.LoginResponse;
import com.siperes.siperes.dto.response.RefreshTokenResponse;
import com.siperes.siperes.dto.response.RegisterResponse;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken();
}