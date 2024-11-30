package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.LoginRequest;
import com.spiceswap.spiceswap.dto.request.RegisterRequest;
import com.spiceswap.spiceswap.dto.response.LoginResponse;
import com.spiceswap.spiceswap.dto.response.RefreshTokenResponse;
import com.spiceswap.spiceswap.dto.response.RegisterResponse;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken();
}