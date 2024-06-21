package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.CreateModificationReqRequest;
import com.siperes.siperes.dto.response.MyCopyRecipeResponse;
import com.siperes.siperes.dto.response.CreateModificationReqResponse;
import com.siperes.siperes.dto.response.InModificationReqResponse;
import com.siperes.siperes.dto.response.OutModificationReqResponse;
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
