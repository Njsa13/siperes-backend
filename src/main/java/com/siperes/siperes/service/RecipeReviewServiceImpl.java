package com.siperes.siperes.service;

import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.dto.request.CreateRecipeReviewRequest;
import com.siperes.siperes.dto.response.RecipeReviewResponse;
import com.siperes.siperes.dto.response.WriteRecipeReviewResponse;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.exception.DataConflictException;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ForbiddenException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.Recipe;
import com.siperes.siperes.model.RecipeReview;
import com.siperes.siperes.model.User;
import com.siperes.siperes.model.key.RecipeReviewKey;
import com.siperes.siperes.repository.RecipeRepository;
import com.siperes.siperes.repository.RecipeReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeReviewServiceImpl implements RecipeReviewService{
    private final RecipeReviewRepository recipeReviewRepository;
    private final RecipeRepository recipeRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public WriteRecipeReviewResponse writeRecipeReview(CreateRecipeReviewRequest request) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(request.getRecipeSlug(), EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            if (recipe.getUser().equals(user)) {
                throw new ForbiddenException(FORBIDDEN_REVIEW);
            }
            RecipeReview existsRecipeReview = recipeReviewRepository.findFirstByUserIdAndRecipeSlug(user, request.getRecipeSlug())
                    .orElse(null);
            RecipeReviewKey key = RecipeReviewKey.builder()
                    .userId(user.getId())
                    .recipeId(recipe.getId())
                    .build();
            RecipeReview recipeReview = RecipeReview.builder()
                    .key(key)
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .isEdit(existsRecipeReview != null)
                    .user(user)
                    .recipe(recipe)
                    .build();
            if (recipeReview.equals(existsRecipeReview)) {
                throw new DataConflictException(CONFLICT_REVIEW);
            }
            recipeReview = recipeReviewRepository.save(recipeReview);
            Double totalRating = Optional.ofNullable(recipeReviewRepository.averageByRecipeId(recipe.getId()))
                    .orElse(0.0);
            long totalReviewers = recipeReviewRepository.countByRecipeId(recipe.getId());
            recipe.setTotalRating(totalRating);
            recipe.setTotalReviewers((int) totalReviewers);
            recipe.setPopularityRate((totalRating * 0.5) + (totalReviewers * 0.3) + (recipe.getTotalBookmarks() * 0.2));
            recipe = recipeRepository.save(recipe);
            return WriteRecipeReviewResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .username(user.getUserName())
                    .rating(recipeReview.getRating())
                    .comment(recipeReview.getComment())
                    .createdAt(Optional.ofNullable(recipeReview.getCreatedAt())
                            .map(LocalDateTime::toLocalDate)
                            .orElse(null))
                    .updatedAt(recipeReview.getUpdateAt().toLocalDate())
                    .totalRating(recipe.getTotalRating())
                    .totalReviewers(recipe.getTotalReviewers())
                    .isEdit(recipeReview.getIsEdit())
                    .build();
        } catch (DataNotFoundException | ForbiddenException | DataConflictException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_WRITE_REVIEW);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeReviewResponse getWriteRecipeReviewDetail(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            RecipeReview recipeReview = recipeReviewRepository.findFirstByUserIdAndRecipeSlug(user, recipeSlug)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_REVIEW_NOT_FOUND));
            return RecipeReviewResponse.builder()
                    .username(user.getUserName())
                    .rating(recipeReview.getRating())
                    .comment(recipeReview.getComment())
                    .isEdit(recipeReview.getIsEdit())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_REVIEW);
        }
    }

    @Override
    @Transactional
    public void deleteRecipeReview(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            if (recipe.getUser().equals(user)) {
                throw new ForbiddenException(FORBIDDEN_REVIEW);
            }
            RecipeReview recipeReview = recipeReviewRepository.findFirstByUserIdAndRecipeId(user.getId(), recipe.getId())
                    .orElseThrow(() -> new DataNotFoundException(FORBIDDEN_NOT_EXIST_REVIEW));
            user.getRecipeReviews().remove(recipeReview);
            recipe.getRecipeReviews().remove(recipeReview);
            recipeReviewRepository.deleteByUserIdAndRecipeId(user.getId(), recipe.getId());
            Double totalRating = Optional.ofNullable(recipeReviewRepository.averageByRecipeId(recipe.getId()))
                    .orElse(0.0);
            long totalReviewers = recipeReviewRepository.countByRecipeId(recipe.getId());
            recipe.setTotalRating(totalRating);
            recipe.setTotalReviewers((int) totalReviewers);
            recipe.setPopularityRate((totalRating * 0.5) + (totalReviewers * 0.3) + (recipe.getTotalBookmarks() * 0.2));
            recipeRepository.save(recipe);
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_DELETE_REVIEW);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeReviewResponse> getAllRecipeReview(String recipeSlug, Pageable pageable) {
        try {
            Page<RecipeReview> recipeReviewPage = Optional.ofNullable(recipeReviewRepository.findByRecipeSlug(recipeSlug, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_REVIEW_NOT_FOUND));
            return recipeReviewPage.map(recipeReview -> RecipeReviewResponse.builder()
                    .username(recipeReview.getUser().getUserName())
                    .rating(recipeReview.getRating())
                    .comment(recipeReview.getComment())
                    .isEdit(recipeReview.getIsEdit())
                    .build());
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_DELETE_REVIEW);
        }
    }
}
