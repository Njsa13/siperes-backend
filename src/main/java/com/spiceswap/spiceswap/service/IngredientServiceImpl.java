package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.common.util.CheckDataUtil;
import com.spiceswap.spiceswap.common.util.ImageUtil;
import com.spiceswap.spiceswap.common.util.SlugUtil;
import com.spiceswap.spiceswap.dto.request.CreateIngredientRequest;
import com.spiceswap.spiceswap.dto.request.UpdateIngredientRequest;
import com.spiceswap.spiceswap.dto.response.AdminIngredientResponse;
import com.spiceswap.spiceswap.dto.response.IngredientResponse;
import com.spiceswap.spiceswap.exception.DataConflictException;
import com.spiceswap.spiceswap.exception.DataNotFoundException;
import com.spiceswap.spiceswap.exception.ServiceBusinessException;
import com.spiceswap.spiceswap.model.Ingredient;
import com.spiceswap.spiceswap.model.IngredientDetail;
import com.spiceswap.spiceswap.repository.IngredientDetailRepository;
import com.spiceswap.spiceswap.repository.IngredientRepository;
import com.spiceswap.spiceswap.repository.specification.IngredientSpecification;
import com.spiceswap.spiceswap.common.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService{
    private final IngredientRepository ingredientRepository;
    private final IngredientDetailRepository ingredientDetailRepository;
    private final SlugUtil slugUtil;
    private final ImageUtil imageUtil;
    private final CheckDataUtil checkDataUtil;

    @Override
    @Transactional
    public AdminIngredientResponse addNewIngredient(CreateIngredientRequest request) {
        try {
            CompletableFuture<String> futureSlug = slugUtil.toSlug("ingredients", "ingredient_slug", request.getIngredientName());
            CompletableFuture<String> thumbnailImageLink = imageUtil.base64UploadImage(request.getImage());
            Ingredient ingredient = Ingredient.builder()
                    .ingredientSlug(futureSlug.join())
                    .ingredientName(request.getIngredientName())
                    .imageLink(thumbnailImageLink.join())
                    .build();
            ingredient = ingredientRepository.save(ingredient);
            return AdminIngredientResponse.builder()
                    .ingredientSlug(ingredient.getIngredientSlug())
                    .ingredientName(ingredient.getIngredientName())
                    .imageLink(ingredient.getImageLink())
                    .createdAt(ingredient.getCreatedAt())
                    .build();
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_CREATE_INGREDIENT);
        }
    }

    @Override
    @Transactional
    public AdminIngredientResponse updateIngredient(String ingredientSlug, UpdateIngredientRequest request) {
        try {
            Ingredient ingredient = ingredientRepository.findFirstByIngredientSlug(ingredientSlug)
                    .orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessage.INGREDIENT_NOT_FOUND));
            checkDataUtil.checkDataField("ingredients", "ingredient_name", request.getIngredientName(), "ingredient_id", ingredient.getId());
            ingredient.setIngredientName(request.getIngredientName());
            Optional.ofNullable(request.getImage())
                    .ifPresent(image -> {
                        imageUtil.deleteImage(ingredient.getImageLink());
                        String thumbnailImageLink = imageUtil.base64UploadImage(image).join();
                        ingredient.setImageLink(thumbnailImageLink);
                    });
            Ingredient finalIngredient = ingredientRepository.save(ingredient);
            return AdminIngredientResponse.builder()
                    .ingredientSlug(finalIngredient.getIngredientSlug())
                    .ingredientName(finalIngredient.getIngredientName())
                    .imageLink(finalIngredient.getImageLink())
                    .updateAt(finalIngredient.getUpdateAt())
                    .build();
        } catch (DataNotFoundException | DataConflictException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_UPDATE_INGREDIENT);
        }
    }

    @Override
    @Transactional
    public void deleteIngredient(String ingredientSlug) {
        try {
            ingredientRepository.findFirstByIngredientSlug(ingredientSlug)
                    .ifPresentOrElse(ingredient -> {
                        List<IngredientDetail> ingredientDetails = ingredient.getIngredientDetails().stream()
                                .peek(val -> val.setIngredient(null)).toList();
                        ingredientDetailRepository.saveAll(ingredientDetails);
                        ingredientRepository.delete(ingredient);
                        Optional.ofNullable(ingredient.getImageLink())
                                .ifPresent(imageUtil::deleteImage);
                    }, () -> {
                        throw new DataNotFoundException(Constants.ErrorMessage.INGREDIENT_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_DELETE_INGREDIENT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientResponse> getAllIngredient(String keyword, Pageable pageable) {
        try {
            Page<Ingredient> ingredientPage = Optional.ofNullable(ingredientRepository.findAll(IngredientSpecification.ingredientNameContains(keyword), pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessage.INGREDIENT_NOT_FOUND));
            return ingredientPage.map(ingredient -> IngredientResponse.builder()
                    .ingredientSlug(ingredient.getIngredientSlug())
                    .ingredientName(ingredient.getIngredientName())
                    .imageLink(ingredient.getImageLink())
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_GET_INGREDIENT_LIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredientList() {
        try {
            List<Ingredient> ingredients = Optional.of(ingredientRepository.findAll(PageRequest.of(0, 4)).stream().toList())
                    .orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessage.INGREDIENT_NOT_FOUND));
            return ingredients.stream()
                    .map(ingredient -> IngredientResponse.builder()
                            .ingredientSlug(ingredient.getIngredientSlug())
                            .ingredientName(ingredient.getIngredientName())
                            .imageLink(ingredient.getImageLink())
                            .build())
                    .collect(Collectors.toList());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_GET_INGREDIENT_LIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminIngredientResponse> getAllIngredientForAdmin(String keyword, Pageable pageable) {
        try {
            Page<Ingredient> ingredientPage = Optional.ofNullable(ingredientRepository.findAll(IngredientSpecification.ingredientNameContains(keyword), pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(Constants.ErrorMessage.INGREDIENT_NOT_FOUND));
            return ingredientPage.map(ingredient -> AdminIngredientResponse.builder()
                    .ingredientSlug(ingredient.getIngredientSlug())
                    .ingredientName(ingredient.getIngredientName())
                    .imageLink(ingredient.getImageLink())
                    .createdAt(ingredient.getCreatedAt())
                    .updateAt(ingredient.getUpdateAt())
                    .build());
        } catch (DataNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(Constants.ErrorMessage.FAILED_GET_INGREDIENT_LIST);
        }
    }
}
