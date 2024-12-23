package com.spiceswap.spiceswap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String token;
}
