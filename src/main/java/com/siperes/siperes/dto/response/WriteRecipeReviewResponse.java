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
public class WriteRecipeReviewResponse {
    private String recipeSlug;
    private String username;
    private Integer rating;
    private String comment;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Double totalRating;
    private Integer totalReviewers;
    private Boolean isEdit;
}
