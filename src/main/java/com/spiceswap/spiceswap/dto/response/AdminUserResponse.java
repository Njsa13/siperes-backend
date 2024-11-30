package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spiceswap.spiceswap.enumeration.EnumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserResponse {
    private String username;
    private String name;
    private String email;
    private Boolean isVerifiedEmail;
    private LocalDateTime dateOfBirth;
    private EnumStatus status;
    private LocalDateTime lastLogin;
    private String profileImageLink;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
