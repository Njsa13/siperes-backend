package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.validation.Base64Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileImageRequest {
    @NotBlank
    @Base64Image
    private String profileImage;
}
