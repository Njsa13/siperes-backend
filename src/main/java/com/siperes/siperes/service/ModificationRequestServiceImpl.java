package com.siperes.siperes.service;

import com.siperes.siperes.common.util.JwtUtil;
import com.siperes.siperes.common.util.SlugUtil;
import com.siperes.siperes.common.util.StringUtil;
import com.siperes.siperes.dto.request.CreateModificationReqRequest;
import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumRequestStatus;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.exception.DataConflictException;
import com.siperes.siperes.exception.DataNotFoundException;
import com.siperes.siperes.exception.ForbiddenException;
import com.siperes.siperes.exception.ServiceBusinessException;
import com.siperes.siperes.model.*;
import com.siperes.siperes.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModificationRequestServiceImpl implements ModificationRequestService {
    private final RecipeRepository recipeRepository;
    private final IngredientDetailRepository ingredientDetailRepository;
    private final StepRepository stepRepository;
    private final ModificationRequestRepository modificationRequestRepository;
    private final CopyDetailRepository copyDetailRepository;
    private final RecipeHistoryRepository recipeHistoryRepository;
    private final JwtUtil jwtUtil;
    private final SlugUtil slugUtil;
    private final StringUtil stringUtil;

    @Override
    @Transactional
    public CreateModificationReqResponse createModificationRequest(CreateModificationReqRequest request) {
        try {
            User user = jwtUtil.getUser();
            CopyDetail copyDetail = copyDetailRepository.findByCopyRecipeUserAndSlug(user, request.getFromRecipeSlug(), EnumStatus.ACTIVE, EnumStatus.ACTIVE)
                    .orElseThrow(() -> new DataNotFoundException(ORIGINAL_RECIPE_NOT_FOUND));
            List<RecipeHistory> recipeHistories = modificationRequestRepository.findFirstByRequestStatusAndCopyDetailOrderByCreatedAtDesc(EnumRequestStatus.APPROVED, copyDetail)
                    .map( val -> val.getCopyDetail().getCopyRecipe().getRecipeHistories().stream()
                            .filter(recipeHistory -> recipeHistory.getCreatedAt().isAfter(val.getCreatedAt()))
                            .collect(Collectors.toList()))
                    .orElse(new ArrayList<>(copyDetail.getCopyRecipe().getRecipeHistories()));
            if (recipeHistories.isEmpty()) {
                throw new ForbiddenException(FORBIDDEN_EMPTY_HISTORIES);
            }
            Recipe originalRecipe = copyDetail.getOriginalRecipe();
            Recipe copyRecipe = copyDetail.getCopyRecipe();
            if (originalRecipe.getVisibility().equals(EnumVisibility.PRIVATE)) {
                throw new ForbiddenException(FORBIDDEN_PRIVATE_REQUEST);
            }
            if (modificationRequestRepository.existsByStatus(EnumRequestStatus.WAITING, originalRecipe, copyRecipe)) {
                throw new DataConflictException(EXISTS_REQUEST);
            }
            ModificationRequest modificationRequest = ModificationRequest.builder()
                    .message(request.getMessage())
                    .requestStatus(EnumRequestStatus.WAITING)
                    .createdAt(LocalDateTime.now())
                    .copyDetail(copyDetail)
                    .build();
            ModificationRequest finalModificationRequest =  modificationRequestRepository.save(modificationRequest);
            return CreateModificationReqResponse.builder()
                    .message(finalModificationRequest.getMessage())
                    .requestStatus(finalModificationRequest.getRequestStatus())
                    .createdAt(finalModificationRequest.getCreatedAt().toLocalDate())
                    .fromRecipeSlug(finalModificationRequest.getCopyDetail().getCopyRecipe().getRecipeSlug())
                    .fromRecipeName(finalModificationRequest.getCopyDetail().getCopyRecipe().getRecipeName())
                    .toRecipeSlug(finalModificationRequest.getCopyDetail().getOriginalRecipe().getRecipeSlug())
                    .toRecipeName(finalModificationRequest.getCopyDetail().getOriginalRecipe().getRecipeName())
                    .requestFrom(user.getUserName())
                    .requestTo(finalModificationRequest.getCopyDetail().getOriginalRecipe().getUser().getUserName())
                    .build();
        } catch (DataNotFoundException | ForbiddenException | DataConflictException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_CREATE_MODIFICATION_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutModificationReqResponse> getOutModificationRequest(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<ModificationRequest> modificationRequestPage = Optional.ofNullable(modificationRequestRepository.findByUserOfCopyRecipe(user, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(MODIFICATION_REQUEST_NOT_FOUND));
            return modificationRequestPage.map(modificationRequest -> {
                if (modificationRequest.getRequestStatus().equals(EnumRequestStatus.FAILED)) {
                    return OutModificationReqResponse.builder()
                            .message(modificationRequest.getMessage())
                            .requestStatus(EnumRequestStatus.FAILED)
                            .createdAt(modificationRequest.getCreatedAt().toLocalDate())
                            .fromRecipeSlug(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeSlug())
                            .fromRecipeName(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeName())
                            .requestTo(modificationRequest.getCopyDetail().getOriginalRecipe().getUser().getUserName())
                            .build();
                } else {
                    return OutModificationReqResponse.builder()
                            .message(modificationRequest.getMessage())
                            .requestStatus(modificationRequest.getRequestStatus())
                            .createdAt(modificationRequest.getCreatedAt().toLocalDate())
                            .fromRecipeSlug(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeSlug())
                            .fromRecipeName(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeName())
                            .toRecipeSlug(modificationRequest.getCopyDetail().getOriginalRecipe().getRecipeSlug())
                            .toRecipeName(modificationRequest.getCopyDetail().getOriginalRecipe().getRecipeName())
                            .requestTo(modificationRequest.getCopyDetail().getOriginalRecipe().getUser().getUserName())
                            .build();
                }
            });
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_MODIFICATION_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InModificationReqResponse> getInModificationRequest(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<ModificationRequest> modificationRequestPage = Optional.ofNullable(modificationRequestRepository.findByUserOfOriginalRecipeAndRequestStatusIsNot(user, EnumRequestStatus.FAILED, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(MODIFICATION_REQUEST_NOT_FOUND));
            return modificationRequestPage.map(modificationRequest -> InModificationReqResponse.builder()
                    .message(modificationRequest.getMessage())
                    .requestStatus(modificationRequest.getRequestStatus())
                    .createdAt(modificationRequest.getCreatedAt().toLocalDate())
                    .fromRecipeSlug(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeSlug())
                    .fromRecipeName(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeName())
                    .toRecipeSlug(modificationRequest.getCopyDetail().getOriginalRecipe().getRecipeSlug())
                    .toRecipeName(modificationRequest.getCopyDetail().getOriginalRecipe().getRecipeName())
                    .requestFrom(modificationRequest.getCopyDetail().getCopyRecipe().getUser().getUserName())
                    .canApprove(modificationRequest.getRequestStatus().equals(EnumRequestStatus.WAITING))
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_MODIFICATION_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MyCopyRecipeResponse> getCopyRecipe(Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<CopyDetail> copyDetailPage = Optional.ofNullable(copyDetailRepository.findByCopyRecipeUser(user,EnumRecipeType.COPY, EnumStatus.ACTIVE, EnumStatus.ACTIVE, pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(RECIPE_NOT_FOUND));
            return copyDetailPage.map(copyDetail -> MyCopyRecipeResponse.builder()
                    .fromRecipeSlug(copyDetail.getCopyRecipe().getRecipeSlug())
                    .fromRecipeName(copyDetail.getCopyRecipe().getRecipeSlug())
                    .fromRecipeName(copyDetail.getCopyRecipe().getRecipeName())
                    .toRecipeName(copyDetail.getOriginalRecipe().getRecipeName())
                    .thumbnailImageLink(copyDetail.getCopyRecipe().getThumbnailImageLink())
                    .createdAt(copyDetail.getCreatedAt().toLocalDate())
                    .requestTo(copyDetail.getOriginalRecipe().getUser().getUserName())
                    .isWaiting(copyDetail.getModificationRequests().stream()
                            .anyMatch(val -> val.getRequestStatus().equals(EnumRequestStatus.WAITING)))
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_GET_RECIPE_COPY);
        }
    }

    @Override
    @Transactional
    public void approveRequest(String fromRecipeSlug) {
        try {
            User user = jwtUtil.getUser();
            ModificationRequest modificationRequest = modificationRequestRepository.findByCopyRecipeUserAndSlug(user, fromRecipeSlug, EnumStatus.ACTIVE, EnumStatus.ACTIVE, EnumRequestStatus.WAITING)
                    .orElseThrow(() -> new DataNotFoundException(MODIFICATION_REQUEST_NOT_FOUND));
            if (!modificationRequest.getCopyDetail().getCopyRecipe().getRecipeType().equals(EnumRecipeType.COPY)) {
                throw new ForbiddenException(FORBIDDEN_APPROVE_REQUEST);
            }
            Recipe originalRecipe = modificationRequest.getCopyDetail().getOriginalRecipe();
            Recipe copyRecipe = modificationRequest.getCopyDetail().getCopyRecipe();
            originalRecipe.setPortion(copyRecipe.getPortion());
            Recipe finalRecipe = recipeRepository.save(originalRecipe);

            List<IngredientDetail> ingredientDetails = copyRecipe.getIngredientDetails().stream()
                    .sorted(Comparator.comparing(IngredientDetail::getIngredientName))
                    .toList();
            List<Step> steps = copyRecipe.getSteps().stream()
                    .sorted(Comparator.comparingInt(Step::getNumberStep))
                    .toList();
            List<RecipeHistory> recipeHistories = modificationRequestRepository.findFirstByRequestStatusAndCopyDetailOrderByCreatedAtDesc(EnumRequestStatus.APPROVED, modificationRequest.getCopyDetail())
                    .map( val -> val.getCopyDetail().getCopyRecipe().getRecipeHistories().stream()
                            .filter(recipeHistory -> recipeHistory.getCreatedAt().isAfter(val.getCreatedAt()))
                            .collect(Collectors.toList()))
                    .orElse(new ArrayList<>(modificationRequest.getCopyDetail().getCopyRecipe().getRecipeHistories()));

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
            List<CompletableFuture<String>> futureHistorySlug = recipeHistories.stream()
                    .map(recipeHistory -> slugUtil.toSlug(
                            "recipe_histories",
                            "slug_history",
                            finalRecipe.getRecipeName()+" history"))
                    .toList();

            List<IngredientDetail> newIngredientDetails = IntStream.range(0, futureIngredientDetailSlugs.size())
                    .mapToObj(i -> IngredientDetail.builder()
                            .ingredientDetailSlug(futureIngredientDetailSlugs.get(i).join())
                            .ingredientName(ingredientDetails.get(i).getIngredientName())
                            .dose(ingredientDetails.get(i).getDose())
                            .ingredient(ingredientDetails.get(i).getIngredient())
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            List<Step> newSteps = IntStream.range(0, futureStepSlugs.size())
                    .mapToObj(i -> Step.builder()
                            .stepSlug(futureStepSlugs.get(i).join())
                            .numberStep(i+1)
                            .stepDescription(steps.get(i).getStepDescription())
                            .recipe(finalRecipe)
                            .build())
                    .toList();
            List<RecipeHistory> newRecipeHistory = IntStream.range(0, futureHistorySlug.size())
                    .mapToObj(i -> RecipeHistory.builder()
                            .historySlug(futureHistorySlug.get(i).join())
                            .previousRecipe(recipeHistories.get(i).getPreviousRecipe())
                            .currentRecipe(recipeHistories.get(i).getCurrentRecipe())
                            .createdBy(recipeHistories.get(i).getCreatedBy())
                            .recipe(finalRecipe)
                            .build())
                    .toList();

            modificationRequest.setRequestStatus(EnumRequestStatus.APPROVED);

            ingredientDetailRepository.deleteByRecipeId(originalRecipe.getId());
            stepRepository.deleteByRecipeId(originalRecipe.getId());
            ingredientDetailRepository.saveAll(newIngredientDetails);
            stepRepository.saveAll(newSteps);
            recipeHistoryRepository.saveAll(newRecipeHistory);

            modificationRequestRepository.save(modificationRequest);
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_TO_APPROVE_REQUEST);
        }
    }

    @Override
    @Transactional
    public void rejectRequest(String fromRecipeSlug) {
        try {
            User user = jwtUtil.getUser();
            ModificationRequest modificationRequest = modificationRequestRepository.findByCopyRecipeUserAndSlug(user, fromRecipeSlug, EnumStatus.ACTIVE, EnumStatus.ACTIVE, EnumRequestStatus.WAITING)
                    .orElseThrow(() -> new DataNotFoundException(MODIFICATION_REQUEST_NOT_FOUND));
            if (!modificationRequest.getCopyDetail().getCopyRecipe().getRecipeType().equals(EnumRecipeType.COPY)) {
                throw new ForbiddenException(FORBIDDEN_APPROVE_REQUEST);
            }
            modificationRequest.setRequestStatus(EnumRequestStatus.REJECTED);
            modificationRequestRepository.save(modificationRequest);
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(FAILED_TO_REJECT_REQUEST);
        }
    }
}
