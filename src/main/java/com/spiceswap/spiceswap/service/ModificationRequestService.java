package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.CreateModificationReqRequest;
import com.spiceswap.spiceswap.dto.response.MyCopyRecipeResponse;
import com.spiceswap.spiceswap.dto.response.CreateModificationReqResponse;
import com.spiceswap.spiceswap.dto.response.InModificationReqResponse;
import com.spiceswap.spiceswap.dto.response.OutModificationReqResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModificationRequestService {
    CreateModificationReqResponse createModificationRequest(CreateModificationReqRequest request);
    Page<OutModificationReqResponse> getOutModificationRequest(Pageable pageable);
    Page<InModificationReqResponse> getInModificationRequest(Pageable pageable);
    Page<MyCopyRecipeResponse> getCopyRecipe(Pageable pageable);
    void approveRequest(String fromRecipeSlug);
    void rejectRequest(String fromRecipeSlug);
}
