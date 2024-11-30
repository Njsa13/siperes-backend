package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CreateIngredientDetailResponse {
    private String ingredientDetailSlug;
    private String ingredientName;
    private String dose;
    private LocalDateTime createdAt;
    private IngredientResponse ingredientResponse;
}
