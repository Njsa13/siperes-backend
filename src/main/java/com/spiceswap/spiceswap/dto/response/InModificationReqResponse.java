package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spiceswap.spiceswap.enumeration.EnumRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InModificationReqResponse {
    private String message;
    private EnumRequestStatus requestStatus;
    private LocalDate createdAt;
    private String fromRecipeSlug;
    private String fromRecipeName;
    private String toRecipeSlug;
    private String toRecipeName;
    private String requestFrom;
    private Boolean canApprove;
}
