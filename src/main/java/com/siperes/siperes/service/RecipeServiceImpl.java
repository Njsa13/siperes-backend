package com.siperes.siperes.service;

import com.siperes.siperes.common.util.ImageUtil;
import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.common.util.SlugUtil;
import com.siperes.siperes.common.util.StringUtil;
import com.siperes.siperes.dto.request.CreateRecipeRequest;
import com.siperes.siperes.dto.request.SetRecipeRequest;
import com.siperes.siperes.dto.request.UpdateIngredientDetailRequest;
import com.siperes.siperes.dto.request.UpdateRecipeRequest;
import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.enumeration.*;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ForbiddenException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.*;
import com.siperes.siperes.model.json.IngredientDetailJson;
import com.siperes.siperes.model.json.RecipeDetailJson;
import com.siperes.siperes.model.json.StepJson;
import com.siperes.siperes.repository.*;
import com.siperes.siperes.repository.specification.RecipeSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final ImageUtil imageUtil;
    private final JwtUtil jwtUtil;
    private final SlugUtil slugUtil;
    private final StringUtil stringUtil;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientDetailRepository ingredientDetailRepository;
    private final StepRepository stepRepository;
    private final RecipeHistoryRepository recipeHistoryRepository;
    private final UserRepository userRepository;
    private final CopyDetailRepository copyDetailRepository;
    private final ModificationRequestRepository modificationRequestRepository;

    @Transactional
    @Override
    public CreateRecipeResponse addRecipe(CreateRecipeRequest recipeRequest) {
        try {
            CompletableFuture<String> futureRecipeSlug = slugUtil.toSlug("recipes", "recipe_slug", recipeRequest.getRecipeName());
            List<CompletableFuture<String>> futureIngredientDetailSlugs = recipeRequest.getIngredientDetailRequests().stream()
                    .map(ingredientDetailRequest -> slugUtil.toSlug(
                            "ingredient_details",
                            "ingredient_detail_slug",
                            ingredientDetailRequest.getIngredientName()))
                    .toList();
            List<CompletableFuture<String>> futureStepSlugs = recipeRequest.getStepRequests().stream()
                    .map(stepRequest -> slugUtil.toSlug(
                            "steps",
                            "step_slug",
                            stringUtil.getFirstTwoWords(stepRequest.getStepDescription())))
                    .toList();
            CompletableFuture<String> thumbnailImageLink = imageUtil.base64UploadImage(recipeRequest.getThumbnailImage());
            User user = jwtUtil.getUser();
            Recipe recipe = Recipe.builder()
                    .recipeSlug(futureRecipeSlug.join())
                    .recipeName(recipeRequest.getRecipeName())
                    .about(recipeRequest.getAbout())
                    .thumbnailImageLink(thumbnailImageLink.join())
                    .portion(recipeRequest.getPortion())
                    .totalRating(0.0)
                    .visibility(recipeRequest.getVisibility())
                    .recipeType(EnumRecipeType.ORIGINAL)
                    .status(EnumStatus.ACTIVE)
                    .user(user)
                    .build();
            Recipe finalRecipe = recipeRepository.save(recipe);
            List<IngredientDetail> ingredientDetails = IntStream.range(0, futureIngredientDetailSlugs.size())
                    .mapToObj(i -> IngredientDetail.builder()
                            .ingredientDetailSlug(futureIngredientDetailSlugs.get(i).join())
                            .ingredientName(recipeRequest.getIngredientDetailRequests().get(i).getIngredientName())
                            .dose(recipeRequest.getIngredientDetailRequests().get(i).getDose())
                            .ingredient(getIngredientIfExists(recipeRequest.getIngredientDetailRequests().get(i).getIngredientName().toLowerCase()))
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            ingredientDetails = ingredientDetailRepository.saveAll(ingredientDetails);
            List<Step> steps = IntStream.range(0, futureStepSlugs.size())
                    .mapToObj(i -> Step.builder()
                            .stepSlug(futureStepSlugs.get(i).join())
                            .numberStep(i+1)
                            .stepDescription(recipeRequest.getStepRequests().get(i).getStepDescription())
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            steps = stepRepository.saveAll(steps);
            return CreateRecipeResponse.builder()
                    .recipeSlug(finalRecipe.getRecipeSlug())
                    .recipeName(finalRecipe.getRecipeName())
                    .about(finalRecipe.getAbout())
                    .thumbnailImageLink(finalRecipe.getThumbnailImageLink())
                    .portion(finalRecipe.getPortion())
                    .totalRating(finalRecipe.getTotalRating())
                    .visibility(finalRecipe.getVisibility())
                    .recipeType(finalRecipe.getRecipeType())
                    .status(finalRecipe.getStatus())
                    .createdAt(finalRecipe.getCreatedAt())
                    .createIngredientDetailResponses(ingredientDetails.stream()
                            .map(ingredientDetail -> CreateIngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .createdAt(ingredientDetail.getCreatedAt())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(CreateIngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .createStepResponses(steps.stream()
                            .map(step -> CreateStepResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .createdAt(step.getCreatedAt())
                                    .build())
                            .sorted(Comparator.comparingInt(CreateStepResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_ADD_RECIPE);
        }
    }

    @Transactional
    @Override
    public UpdateRecipeResponse updateRecipe(String recipeSlug, UpdateRecipeRequest recipeRequest) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            Integer previousPortion = recipe.getPortion();
            recipe.setPortion(recipeRequest.getPortion());
            Recipe finalRecipe = recipeRepository.save(recipe);
            List<CompletableFuture<IngredientDetail>> futureIngredientDetails = recipeRequest.getIngredientDetailRequests().stream()
                    .map(ingredientDetailRequest -> getIngredientDetailCompletableFuture(finalRecipe, ingredientDetailRequest))
                    .toList();
            List<CompletableFuture<Step>> futureSteps = IntStream.range(0, recipeRequest.getStepRequests().size())
                    .mapToObj(i -> getStepCompletableFuture(recipeRequest, finalRecipe, i))
                    .toList();
            List<IngredientDetail> ingredientDetails = futureIngredientDetails.stream()
                    .map(CompletableFuture::join).toList();
            List<Step> steps = futureSteps.stream()
                    .map(CompletableFuture::join).toList();

            List<IngredientDetail> previousIngredientDetails = Collections.emptyList();
            List<IngredientDetail> currentIngredientDetails = Collections.emptyList();
            boolean isIngredientDetailNotSame = !recipe.getIngredientDetails().equals(new HashSet<>(ingredientDetails));
            if (isIngredientDetailNotSame) {
                previousIngredientDetails = recipe.getIngredientDetails().stream()
                        .filter(ingredientDetail -> !ingredientDetails.contains(ingredientDetail)).toList();
                currentIngredientDetails = ingredientDetails.stream()
                        .filter(ingredientDetail -> !finalRecipe.getIngredientDetails().contains(ingredientDetail)).toList();
            }
            List<Step> previousSteps = Collections.emptyList();
            List<Step> currentSteps = Collections.emptyList();
            boolean isStepNotSame = !recipe.getSteps().equals(new HashSet<>(steps));
            if (isStepNotSame) {
                previousSteps = recipe.getSteps().stream()
                        .filter(step -> !steps.contains(step)).toList();
                currentSteps = steps.stream()
                        .filter(step -> !finalRecipe.getSteps().contains(step)).toList();
            }
            Integer currentPortion = finalRecipe.getPortion();
            boolean isPortionSame = previousPortion.equals(currentPortion);
            if (isPortionSame) {
                previousPortion = null;
                currentPortion = null;
            }
            List<IngredientDetail> deleteIngredientDetails = finalRecipe.getIngredientDetails().stream()
                    .filter(ingredientDetail -> ingredientDetails.stream().noneMatch(val -> val.getIngredientDetailSlug().equals(ingredientDetail.getIngredientDetailSlug())))
                    .collect(Collectors.toList());
            List<Step> deleteSteps = finalRecipe.getSteps().stream()
                    .filter(step -> steps.stream().noneMatch(val -> val.getStepSlug().equals(step.getStepSlug())))
                    .collect(Collectors.toList());
            if (!deleteIngredientDetails.isEmpty()) {
                deleteIngredientDetails.forEach(ingredientDetail -> finalRecipe.getIngredientDetails().remove(ingredientDetail));
                ingredientDetailRepository.deleteAll(deleteIngredientDetails);
            }
            if (!deleteSteps.isEmpty()) {
                deleteSteps.forEach(step -> finalRecipe.getSteps().remove(step));
                stepRepository.deleteAll(deleteSteps);
            }
            if (isIngredientDetailNotSame || isStepNotSame || !isPortionSame) {
                String slugHistory = slugUtil.toSlug(
                        "recipe_histories",
                        "slug_history",
                        finalRecipe.getRecipeName()+" history").join();
                saveRecipeHistory(user, previousPortion, finalRecipe, previousIngredientDetails, currentIngredientDetails, previousSteps, currentSteps, currentPortion, slugHistory);
            }
            List<IngredientDetail> finalIngredientDetails = ingredientDetailRepository.saveAll(ingredientDetails);
            List<Step> finalSteps = stepRepository.saveAll(steps);
            return UpdateRecipeResponse.builder()
                    .recipeSlug(finalRecipe.getRecipeSlug())
                    .portion(finalRecipe.getPortion())
                    .updatedAt(finalRecipe.getUpdateAt())
                    .updateIngredientDetailResponses(finalIngredientDetails.stream()
                            .map(ingredientDetail -> UpdateIngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .updatedAt(ingredientDetail.getUpdateAt())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(UpdateIngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .updateStepResponses(finalSteps.stream()
                            .map(step -> UpdateStepResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .updatedAt(step.getUpdateAt())
                                    .build())
                            .sorted(Comparator.comparingInt(UpdateStepResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_UPDATE_RECIPE);
        }
    }

    private void saveRecipeHistory(User user, Integer previousPortion, Recipe finalRecipe, List<IngredientDetail> previousIngredientDetails, List<IngredientDetail> currentIngredientDetails, List<Step> previousSteps, List<Step> currentSteps, Integer currentPortion, String slugHistory) {
        RecipeHistory recipeHistory = RecipeHistory.builder()
                .historySlug(slugHistory)
                .previousRecipe(RecipeDetailJson.builder()
                        .portion(previousPortion)
                        .ingredientDetailJson(Optional.of(previousIngredientDetails)
                                .map(value -> value.stream()
                                        .map(val -> IngredientDetailJson.builder()
                                                .ingredientDetailSlug(val.getIngredientDetailSlug())
                                                .ingredientName(val.getIngredientName())
                                                .dose(val.getDose())
                                                .build())
                                        .sorted(Comparator.comparing(IngredientDetailJson::getIngredientName))
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList()))
                        .stepJson(Optional.of(previousSteps)
                                .map(value -> value.stream()
                                        .map(val -> StepJson.builder()
                                                .stepSlug(val.getStepSlug())
                                                .numberStep(val.getNumberStep())
                                                .stepDescription(val.getStepDescription())
                                                .build())
                                        .sorted(Comparator.comparingInt(StepJson::getNumberStep))
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList()))
                        .build())
                .currentRecipe(RecipeDetailJson.builder()
                        .portion(currentPortion)
                        .ingredientDetailJson(Optional.of(currentIngredientDetails)
                                .map(value -> value.stream()
                                        .map(val -> IngredientDetailJson.builder()
                                                .ingredientDetailSlug(val.getIngredientDetailSlug())
                                                .ingredientName(val.getIngredientName())
                                                .dose(val.getDose())
                                                .build())
                                        .sorted(Comparator.comparing(IngredientDetailJson::getIngredientName))
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList()))
                        .stepJson(Optional.of(currentSteps)
                                .map(value -> value.stream()
                                        .map(val -> StepJson.builder()
                                                .stepSlug(val.getStepSlug())
                                                .numberStep(val.getNumberStep())
                                                .stepDescription(val.getStepDescription())
                                                .build())
                                        .sorted(Comparator.comparingInt(StepJson::getNumberStep))
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList()))
                        .build())
                .createdBy(user.getUsername())
                .recipe(finalRecipe)
                .build();
        recipeHistoryRepository.save(recipeHistory);
    }

    private CompletableFuture<Step> getStepCompletableFuture(UpdateRecipeRequest recipeRequest, Recipe finalRecipe, int i) {
        return CompletableFuture.supplyAsync(() -> {
            Step step = Optional.ofNullable(recipeRequest.getStepRequests().get(i).getStepSlug())
                    .map(val -> stepRepository.findFirstByStepSlug(recipeRequest.getStepRequests().get(i).getStepSlug())
                            .orElseThrow(() -> new DataNotFoundException(STEP_NOT_FOUND + recipeRequest.getStepRequests().get(i).getStepSlug())))
                    .orElse(Step.builder()
                            .stepSlug(slugUtil.toSlug(
                                    "steps",
                                    "step_slug",
                                    stringUtil.getFirstTwoWords(recipeRequest.getStepRequests().get(i).getStepDescription())).join())
                            .recipe(finalRecipe)
                            .build());
            step.setNumberStep(i + 1);
            step.setStepDescription(recipeRequest.getStepRequests().get(i).getStepDescription());
            return step;
        });
    }

    private CompletableFuture<IngredientDetail> getIngredientDetailCompletableFuture(Recipe finalRecipe, UpdateIngredientDetailRequest ingredientDetailRequest) {
        return CompletableFuture.supplyAsync(() -> {
            IngredientDetail ingredientDetail = Optional.ofNullable(ingredientDetailRequest.getIngredientDetailSlug())
                    .map(val -> ingredientDetailRepository.findFirstByIngredientDetailSlug(ingredientDetailRequest.getIngredientDetailSlug())
                            .orElseThrow(() -> new DataNotFoundException(INGREDIENT_DETAIL_NOT_FOUND + ingredientDetailRequest.getIngredientDetailSlug())))
                    .orElse(IngredientDetail.builder()
                            .ingredientDetailSlug(slugUtil.toSlug(
                                    "ingredient_details",
                                    "ingredient_detail_slug",
                                    ingredientDetailRequest.getIngredientName()).join())
                            .recipe(finalRecipe)
                            .build());
            ingredientDetail.setIngredientName(ingredientDetailRequest.getIngredientName());
            ingredientDetail.setDose(ingredientDetailRequest.getDose());
            ingredientDetail.setIngredient(Optional.ofNullable(ingredientDetail.getIngredient())
                    .orElse(getIngredientIfExists(ingredientDetailRequest.getIngredientName().toLowerCase())));
            return ingredientDetail;
        });
    }

    @Transactional
    @Override
    public SetRecipeResponse setRecipe(String recipeSlug, SetRecipeRequest recipeRequest) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            recipe.setRecipeName(recipeRequest.getRecipeName());
            recipe.setAbout(recipeRequest.getAbout());
            recipe.setVisibility(recipeRequest.getVisibility());
            Optional.ofNullable(recipeRequest.getThumbnailImage())
                    .ifPresent(image -> {
                        imageUtil.deleteImage(recipe.getThumbnailImageLink());
                        String thumbnailImageLink = imageUtil.base64UploadImage(image).join();
                        recipe.setThumbnailImageLink(thumbnailImageLink);
                    });
            Recipe finalRecipe = recipeRepository.save(recipe);
            return SetRecipeResponse.builder()
                    .recipeSlug(finalRecipe.getRecipeSlug())
                    .recipeName(finalRecipe.getRecipeName())
                    .about(finalRecipe.getAbout())
                    .thumbnailImageLink(finalRecipe.getThumbnailImageLink())
                    .visibility(finalRecipe.getVisibility())
                    .updatedAt(finalRecipe.getUpdateAt())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_SET_RECIPE);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UpdateRecipeResponse getUpdateRecipeDetail(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            return UpdateRecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .portion(recipe.getPortion())
                    .updateIngredientDetailResponses(recipe.getIngredientDetails().stream()
                            .map(ingredientDetail -> UpdateIngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .updatedAt(ingredientDetail.getUpdateAt())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(UpdateIngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .updateStepResponses(recipe.getSteps().stream()
                            .map(step -> UpdateStepResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .updatedAt(step.getUpdateAt())
                                    .build())
                            .sorted(Comparator.comparingInt(UpdateStepResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_UPDATE_RECIPE_DETAIL);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SetRecipeResponse getSettingRecipeDetail(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            return SetRecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .about(recipe.getAbout())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .visibility(recipe.getVisibility())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_SETTING_RECIPE_DETAIL);
        }
    }

    @Transactional
    @Override
    public void deleteRecipe(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .ifPresentOrElse(recipe -> {
                        if (!recipe.getCopyRecipeCopyDetails().isEmpty()) {
                            for (CopyDetail copyDetail : recipe.getCopyRecipeCopyDetails()) {
                                List<ModificationRequest> modificationRequests = copyDetail.getModificationRequests().stream()
                                        .peek(val -> {
                                            val.setCopyDetail(null);
                                            modificationRequestRepository.save(val);
                                        })
                                        .filter(val -> val.getRequestStatus().equals(EnumRequestStatus.WAITING)).toList();
                                if (!modificationRequests.isEmpty()) {
                                    modificationRequestRepository.deleteAll(modificationRequests);
                                }
                                copyDetailRepository.delete(copyDetail);
                            }
                        }
                        if (!recipe.getOriginalRecipeCopyDetails().isEmpty()) {
                            recipe.getOriginalRecipeCopyDetails().forEach(copyDetail -> {
                                List<ModificationRequest> modificationRequests = copyDetail.getModificationRequests().stream()
                                        .peek(val -> {
                                            val.setCopyDetail(null);
                                            modificationRequestRepository.save(val);
                                        })
                                        .filter(val -> val.getRequestStatus().equals(EnumRequestStatus.WAITING)).toList();
                                modificationRequestRepository.saveAll(modificationRequests.stream()
                                        .peek(val -> val.setRequestStatus(EnumRequestStatus.FAILED))
                                        .collect(Collectors.toList()));
                            });
                            recipe.getOriginalRecipeCopyDetails().clear();
                        }
                        recipeHistoryRepository.deleteByRecipeId(recipe.getId());
                        recipeRepository.delete(recipe);
                        Optional.ofNullable(recipe.getThumbnailImageLink())
                                .ifPresent(imageUtil::deleteImage);
                    }, () -> {
                        throw new DataNotFoundException(RECIPE_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_DELETE_RECIPE);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MyRecipeResponse> getAllMyRecipes(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<Recipe> recipePage = Optional.of(recipeRepository.findByUserAndStatusOrderByCreatedAtDesc(user, EnumStatus.ACTIVE, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            return recipePage.map(recipe -> MyRecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .totalRating(recipe.getTotalRating())
                    .recipeType(recipe.getRecipeType())
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_ALL_MY_RECIPE);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public MyRecipeDetailResponse getMyRecipeDetail(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            String copyFromSlug = null;
            if (Objects.equals(recipe.getRecipeType(), EnumRecipeType.COPY)) {
                Optional<CopyDetail> copyDetail = recipe.getCopyRecipeCopyDetails().stream().findFirst();
                if (copyDetail.isPresent()) {
                    copyFromSlug = copyDetail.get().getOriginalRecipe().getRecipeSlug();
                }
            }
            return MyRecipeDetailResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .about(recipe.getAbout())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .owner(user.getUsername())
                    .portion(recipe.getPortion())
                    .totalRating(recipe.getTotalRating())
                    .visibility(recipe.getVisibility())
                    .recipeType(recipe.getRecipeType())
                    .copyFromSlug(copyFromSlug)
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .ingredientDetailResponses(recipe.getIngredientDetails().stream()
                            .map(ingredientDetail -> IngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(IngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .stepDetailResponses(recipe.getSteps().stream()
                            .map(step -> StepDetailResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .build())
                            .sorted(Comparator.comparingInt(StepDetailResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_MY_RECIPE_DETAIL);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecipeResponse> getAllBookmarkedRecipes(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<Recipe> recipePage = Optional.of(recipeRepository.findByBookmarksAndStatusAndVisibility(user, EnumStatus.ACTIVE, EnumVisibility.PUBLIC, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            return recipePage.map(recipe -> RecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .totalRating(recipe.getTotalRating())
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .canBookmark(true)
                    .isBookmarked(recipe.getBookmarks().contains(user))
                    .recipeType(recipe.getRecipeType())
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_ALL_BOOKMARKED_RECIPE);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeHistoryListResponse getMyRecipeHistories(String recipeSlug, Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndUserAndStatus(recipeSlug, user, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_NOT_FOUND));
            Page<RecipeHistory> recipeHistoryPage = Optional.of(recipeHistoryRepository.findByRecipe(recipe, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_NOT_FOUND));
            Page<RecipeHistoryResponse> responsePage = recipeHistoryPage
                    .map(recipeHistory -> RecipeHistoryResponse.builder()
                            .slugHistory(recipeHistory.getHistorySlug())
                            .updateAt(recipeHistory.getCreatedAt().toLocalDate())
                            .updatedBy(recipeHistory.getCreatedBy())
                            .build());
            return RecipeHistoryListResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .historyDetailResponses(responsePage)
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_HISTORIES);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public RecipeHistoryDetailResponse getMyRecipeHistoryDetail(String historySlug) {
        try {
            User user = jwtUtil.getUser();
            RecipeHistory recipeHistory = recipeHistoryRepository.findFirstByHistorySlug(historySlug)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_DETAIL_NOT_FOUND));
            if (!recipeHistory.getRecipe().getUser().equals(user) || recipeHistory.getRecipe().getStatus().equals(EnumStatus.INACTIVE)) {
                throw new DataNotFoundException(RECIPE_HISTORY_DETAIL_NOT_FOUND);
            }
            return RecipeHistoryDetailResponse.builder()
                    .historySlug(recipeHistory.getHistorySlug())
                    .updateBy(recipeHistory.getHistorySlug())
                    .updatedAt(recipeHistory.getCreatedAt().toLocalDate())
                    .previousRecipe(recipeHistory.getPreviousRecipe())
                    .currentRecipe(recipeHistory.getCurrentRecipe())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_HISTORY_DETAIL);
        }
    }

    @Transactional
    @Override
    public void createBookmark(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            if (recipe.getVisibility().equals(EnumVisibility.PRIVATE)) {
                throw new ForbiddenException(FORBIDDEN_ACCESS_PRIVATE);
            }
            if (user.equals(recipe.getUser())) {
                throw new ForbiddenException(FORBIDDEN_BOOKMARK);
            }
            user.getBookmarks().add(recipe);
            recipe.getBookmarks().add(user);
            userRepository.save(user);
            recipeRepository.save(recipe);
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_CREATE_BOOKMARK);
        }
    }

    @Transactional
    @Override
    public void deleteBookmark(String recipeSlug) {
        try {
            User user = jwtUtil.getUser();
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            if (recipe.getVisibility().equals(EnumVisibility.PRIVATE)) {
                throw new ForbiddenException(FORBIDDEN_ACCESS_PRIVATE);
            }
            if (user.equals(recipe.getUser())) {
                throw new ForbiddenException(FORBIDDEN_BOOKMARK);
            }
            user.getBookmarks().remove(recipe);
            recipe.getBookmarks().remove(user);
            userRepository.save(user);
            recipeRepository.save(recipe);
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_DELETE_BOOKMARK);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponse> getRecipeList() {
        try {
            List<Recipe> recipes = Optional.ofNullable(recipeRepository.findTop12ByStatusAndVisibilityAndRecipeTypeOrderByTotalRatingDesc(EnumStatus.ACTIVE, EnumVisibility.PUBLIC, EnumRecipeType.ORIGINAL))
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            Boolean isLogin = checkLogin();
            User user = null;
            if (checkLogin()) {
                user = jwtUtil.getUser();
            }
            User finalUser = user;
            return recipes.stream()
                    .map(recipe -> RecipeResponse.builder()
                            .recipeSlug(recipe.getRecipeSlug())
                            .recipeName(recipe.getRecipeName())
                            .thumbnailImageLink(recipe.getThumbnailImageLink())
                            .totalRating(recipe.getTotalRating())
                            .createdAt(recipe.getCreatedAt().toLocalDate())
                            .canBookmark(Optional.ofNullable(finalUser)
                                    .map(val -> !val.getUsername().equals(recipe.getUser().getUsername()))
                                    .orElse(isLogin))
                            .isBookmarked(Optional.ofNullable(finalUser)
                                    .map(val -> recipe.getBookmarks().contains(val))
                                    .orElse(null))
                            .build())
                    .collect(Collectors.toList());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_LIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetail(String recipeSlug) {
        try {
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            Boolean isLogin = checkLogin();
            User user = null;
            boolean isNotAuthorized = true;
            if (isLogin) {
                user = jwtUtil.getUser();
                if (!recipe.getCopyRecipeCopyDetails().isEmpty()) {
                    Optional<CopyDetail> optionalCopyDetail = recipe.getCopyRecipeCopyDetails().stream().findFirst();
                    if (optionalCopyDetail.isPresent()) {
                        boolean isExistsRequest = optionalCopyDetail.get().getModificationRequests().stream()
                                .anyMatch(val -> val.getRequestStatus().equals(EnumRequestStatus.WAITING));
                        isNotAuthorized = !((optionalCopyDetail.get().getOriginalRecipe().getUser().equals(user) && isExistsRequest) || recipe.getUser().equals(user));
                    }
                }
            }
            User finalUser = user;
            if (recipe.getVisibility().equals(EnumVisibility.PRIVATE) && isNotAuthorized) {
                throw new ForbiddenException(FORBIDDEN_ACCESS_PRIVATE);
            }
            String copyFromSlug = null;
            if (Objects.equals(recipe.getRecipeType(), EnumRecipeType.COPY)) {
                Optional<CopyDetail> copyDetail = recipe.getCopyRecipeCopyDetails().stream().findFirst();
                if (copyDetail.isPresent()) {
                    copyFromSlug = copyDetail.get().getOriginalRecipe().getRecipeSlug();
                }
            }
            return RecipeDetailResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .about(recipe.getAbout())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .owner(recipe.getUser().getUsername())
                    .portion(recipe.getPortion())
                    .totalRating(recipe.getTotalRating())
                    .recipeType(recipe.getRecipeType())
                    .copyFromSlug(copyFromSlug)
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .canBookmark(Optional.ofNullable(finalUser)
                            .map(val -> !val.getUsername().equals(recipe.getUser().getUsername()))
                            .orElse(isLogin))
                    .canCopy(Optional.ofNullable(finalUser)
                            .map(val -> !val.getUsername().equals(recipe.getUser().getUsername()))
                            .orElse(isLogin))
                    .isBookmarked(Optional.ofNullable(finalUser)
                            .map(val -> recipe.getBookmarks().contains(val))
                            .orElse(null))
                    .ingredientDetailResponses(recipe.getIngredientDetails().stream()
                            .map(ingredientDetail -> IngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(IngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .stepDetailResponses(recipe.getSteps().stream()
                            .map(step -> StepDetailResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .build())
                            .sorted(Comparator.comparingInt(StepDetailResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_DETAIL);
        }
    }

    @Override
    public Page<RecipeResponse> getCopyRecipeList(String recipeSlug, Pageable pageable) {
        try {
            Page<Recipe> recipePage = Optional.ofNullable(recipeRepository.findByStatusAndVisibilityAndRecipeTypeAndOriginalRecipeSlug(EnumStatus.ACTIVE, EnumVisibility.PUBLIC, EnumRecipeType.COPY, recipeSlug, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            Boolean isLogin = checkLogin();
            User user = null;
            if (checkLogin()) {
                user = jwtUtil.getUser();
            }
            User finalUser = user;
            return recipePage.map(recipe -> RecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .totalRating(recipe.getTotalRating())
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .canBookmark(Optional.ofNullable(finalUser)
                            .map(val -> !val.getUsername().equals(recipe.getUser().getUsername()))
                            .orElse(isLogin))
                    .isBookmarked(Optional.ofNullable(finalUser)
                            .map(val -> recipe.getBookmarks().contains(val))
                            .orElse(null))
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_LIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeHistoryListResponse getRecipeHistories(String recipeSlug, Pageable pageable) {
        try {
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_NOT_FOUND));
            Boolean isLogin = checkLogin();
            boolean isNotAuthorized = true;
            if (isLogin) {
                User user = jwtUtil.getUser();
                if (!recipe.getCopyRecipeCopyDetails().isEmpty()) {
                    Optional<CopyDetail> optionalCopyDetail = recipe.getCopyRecipeCopyDetails().stream().findFirst();
                    if (optionalCopyDetail.isPresent()) {
                        boolean isExistsRequest = optionalCopyDetail.get().getModificationRequests().stream()
                                .anyMatch(val -> val.getRequestStatus().equals(EnumRequestStatus.WAITING));
                        isNotAuthorized = !((optionalCopyDetail.get().getOriginalRecipe().getUser().equals(user) && isExistsRequest) || recipe.getUser().equals(user));
                    }
                }
            }
            if (recipe.getVisibility().equals(EnumVisibility.PRIVATE) && isNotAuthorized) {
                throw new ForbiddenException(FORBIDDEN_ACCESS_PRIVATE);
            }
            Page<RecipeHistory> recipeHistoryPage = Optional.of(recipeHistoryRepository.findByRecipe(recipe, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_NOT_FOUND));
            Page<RecipeHistoryResponse> responsePage = recipeHistoryPage
                    .map(recipeHistory -> RecipeHistoryResponse.builder()
                            .slugHistory(recipeHistory.getHistorySlug())
                            .updateAt(recipeHistory.getCreatedAt().toLocalDate())
                            .updatedBy(recipeHistory.getCreatedBy())
                            .build());
            return RecipeHistoryListResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .historyDetailResponses(responsePage)
                    .build();
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_HISTORIES);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeHistoryDetailResponse getRecipeHistoryDetail(String historySlug) {
        try {
            RecipeHistory recipeHistory = recipeHistoryRepository.findFirstByHistorySlug(historySlug)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_HISTORY_DETAIL_NOT_FOUND));
            if (recipeHistory.getRecipe().getStatus().equals(EnumStatus.INACTIVE)) {
                throw new DataNotFoundException(RECIPE_HISTORY_DETAIL_NOT_FOUND);
            }
            return RecipeHistoryDetailResponse.builder()
                    .historySlug(recipeHistory.getHistorySlug())
                    .updateBy(recipeHistory.getHistorySlug())
                    .updatedAt(recipeHistory.getCreatedAt().toLocalDate())
                    .previousRecipe(recipeHistory.getPreviousRecipe())
                    .currentRecipe(recipeHistory.getCurrentRecipe())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_HISTORY_DETAIL);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeResponse> searchAndSortingRecipe(String keyword, EnumSortBy sortBy, Pageable pageable) {
        try {
            Specification<Recipe> specification = Specification.where(RecipeSpecification.hasRecipeNameOrIngredientName(keyword));
            if (sortBy != null) {
                if (sortBy.equals(EnumSortBy.NEWEST)) {
                    specification = specification.and(RecipeSpecification.orderByCreatedAt(false));
                }
                if (sortBy.equals(EnumSortBy.OLDEST)) {
                    specification = specification.and(RecipeSpecification.orderByCreatedAt(true));
                }
                if (sortBy.equals(EnumSortBy.POPULAR)) {
                    specification = specification.and(RecipeSpecification.orderByRating());
                }
            }
            Page<Recipe> recipePage = Optional.of(recipeRepository.findAll(specification, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            Boolean isLogin = checkLogin();
            User user = null;
            if (checkLogin()) {
                user = jwtUtil.getUser();
            }
            User finalUser = user;
            return recipePage.map(recipe -> RecipeResponse.builder()
                    .recipeSlug(recipe.getRecipeSlug())
                    .recipeName(recipe.getRecipeName())
                    .thumbnailImageLink(recipe.getThumbnailImageLink())
                    .totalRating(recipe.getTotalRating())
                    .createdAt(recipe.getCreatedAt().toLocalDate())
                    .canBookmark(Optional.ofNullable(finalUser)
                            .map(val -> !val.getUsername().equals(recipe.getUser().getUsername()))
                            .orElse(isLogin))
                    .isBookmarked(Optional.ofNullable(finalUser)
                            .map(val -> recipe.getBookmarks().contains(val))
                            .orElse(null))
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_LIST);
        }
    }

    @Override
    @Transactional
    public CreateRecipeResponse copyRecipe(String recipeSlug) {
        try {
            Recipe recipe = recipeRepository.findFirstByRecipeSlugAndStatus(recipeSlug, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            if (recipe.getVisibility().equals(EnumVisibility.PRIVATE)) {
                throw new ForbiddenException(FORBIDDEN_ACCESS_PRIVATE);
            }
            User user = jwtUtil.getUser();
            if (user.equals(recipe.getUser())) {
                throw new ForbiddenException(FORBIDDEN_COPY);
            }
            if (recipeRepository.countCopyRecipe(user, recipe, EnumStatus.ACTIVE) >= 3) {
                throw new ForbiddenException(MAX_COPY_RECIPE);
            }
            CompletableFuture<String> image = imageUtil.downloadImageAsBase64(recipe.getThumbnailImageLink());
            CompletableFuture<String> futureRecipeSlug = slugUtil.toSlug("recipes", "recipe_slug", recipe.getRecipeName());
            List<IngredientDetail> ingredientDetails = recipe.getIngredientDetails().stream().toList();
            List<Step> steps = recipe.getSteps().stream()
                    .sorted(Comparator.comparingInt(Step::getNumberStep))
                    .toList();
            List<CompletableFuture<String>> futureIngredientDetailSlugs = ingredientDetails.stream()
                    .map(ingredientDetail -> slugUtil.toSlug(
                            "ingredient_details",
                            "ingredient_detail_slug",
                            ingredientDetail.getIngredientName()))
                    .toList();
            List<CompletableFuture<String>> futureStepSlugs = steps.stream()
                    .map(step -> slugUtil.toSlug(
                            "steps",
                            "step_slug",
                            stringUtil.getFirstTwoWords(step.getStepDescription())))
                    .toList();
            String imageString = "data:image/jpeg;base64," + image.join();
            CompletableFuture<String> thumbnailImageLink = imageUtil.base64UploadImage(imageString);
            Recipe newRecipe = Recipe.builder()
                    .recipeSlug(futureRecipeSlug.join())
                    .recipeName(recipe.getRecipeName())
                    .about(recipe.getAbout())
                    .thumbnailImageLink(thumbnailImageLink.join())
                    .portion(recipe.getPortion())
                    .totalRating(0.0)
                    .visibility(EnumVisibility.PUBLIC)
                    .recipeType(EnumRecipeType.COPY)
                    .status(EnumStatus.ACTIVE)
                    .user(user)
                    .build();
            Recipe finalRecipe = recipeRepository.save(newRecipe);
            CopyDetail copyDetail = CopyDetail.builder()
                    .originalRecipe(recipe)
                    .copyRecipe(finalRecipe)
                    .build();
            copyDetailRepository.save(copyDetail);
            List<IngredientDetail> newIngredientDetails = IntStream.range(0, futureIngredientDetailSlugs.size())
                    .mapToObj(i -> IngredientDetail.builder()
                            .ingredientDetailSlug(futureIngredientDetailSlugs.get(i).join())
                            .ingredientName(ingredientDetails.get(i).getIngredientName())
                            .dose(ingredientDetails.get(i).getDose())
                            .ingredient(getIngredientIfExists(ingredientDetails.get(i).getIngredientName().toLowerCase()))
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            newIngredientDetails = ingredientDetailRepository.saveAll(newIngredientDetails);
            List<Step> newSteps = IntStream.range(0, futureStepSlugs.size())
                    .mapToObj(i -> Step.builder()
                            .stepSlug(futureStepSlugs.get(i).join())
                            .numberStep(i+1)
                            .stepDescription(steps.get(i).getStepDescription())
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            newSteps = stepRepository.saveAll(newSteps);
            return CreateRecipeResponse.builder()
                    .recipeSlug(finalRecipe.getRecipeSlug())
                    .recipeName(finalRecipe.getRecipeName())
                    .about(finalRecipe.getAbout())
                    .thumbnailImageLink(finalRecipe.getThumbnailImageLink())
                    .portion(finalRecipe.getPortion())
                    .totalRating(finalRecipe.getTotalRating())
                    .visibility(finalRecipe.getVisibility())
                    .recipeType(finalRecipe.getRecipeType())
                    .status(finalRecipe.getStatus())
                    .createdAt(finalRecipe.getCreatedAt())
                    .createIngredientDetailResponses(newIngredientDetails.stream()
                            .map(ingredientDetail -> CreateIngredientDetailResponse.builder()
                                    .ingredientDetailSlug(ingredientDetail.getIngredientDetailSlug())
                                    .ingredientName(ingredientDetail.getIngredientName())
                                    .dose(ingredientDetail.getDose())
                                    .createdAt(ingredientDetail.getCreatedAt())
                                    .ingredientResponse(Optional.ofNullable(ingredientDetail.getIngredient())
                                            .map(ingredient -> IngredientResponse.builder()
                                                    .ingredientSlug(ingredient.getIngredientSlug())
                                                    .ingredientName(ingredient.getIngredientName())
                                                    .imageLink(ingredient.getImageLink())
                                                    .build())
                                            .orElse(null))
                                    .build())
                            .sorted(Comparator.comparing(CreateIngredientDetailResponse::getIngredientName))
                            .collect(Collectors.toList()))
                    .createStepResponses(newSteps.stream()
                            .map(step -> CreateStepResponse.builder()
                                    .stepSlug(step.getStepSlug())
                                    .numberStep(step.getNumberStep())
                                    .stepDescription(step.getStepDescription())
                                    .createdAt(step.getCreatedAt())
                                    .build())
                            .sorted(Comparator.comparingInt(CreateStepResponse::getNumberStep))
                            .collect(Collectors.toList()))
                    .build();
        } catch (DataNotFoundException | ForbiddenException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_COPY_RECIPE);
        }
    }

    private Boolean checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private Ingredient getIngredientIfExists(String ingredientName) {
        return ingredientRepository.findFirstByIngredientName(ingredientName)
                .orElse(null);
    }
}
