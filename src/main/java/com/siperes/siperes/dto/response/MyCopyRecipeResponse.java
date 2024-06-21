package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MyCopyRecipeResponse {
    private String fromRecipeSlug;
    private String fromRecipeName;
    private String toRecipeName;
    private String thumbnailImageLink;
    private LocalDate createdAt;
    private String requestTo;
}
