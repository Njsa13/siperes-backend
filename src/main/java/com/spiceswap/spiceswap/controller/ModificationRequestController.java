package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.CreateModificationReqRequest;
import com.spiceswap.spiceswap.dto.response.CreateModificationReqResponse;
import com.spiceswap.spiceswap.dto.response.MyCopyRecipeResponse;
import com.spiceswap.spiceswap.dto.response.OutModificationReqResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.ModificationRequestService;
import com.spiceswap.spiceswap.common.util.Constants;
import com.spiceswap.spiceswap.dto.response.InModificationReqResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.ModificationRequest.MODIFICATION_REQUEST_PATS, produces = "application/json")
@Tag(name = "Modification Request", description = "Modification Request API")
public class ModificationRequestController {
    private final ModificationRequestService modificationRequestService;

    @PostMapping("/create")
    @Schema(name = "ModificationRequestRequest", description = "ModificationRequest request body")
    @Operation(summary = "Endpoint for making modification request")
    public ResponseEntity<APIResultResponse<CreateModificationReqResponse>> createModificationRequest(@RequestBody @Valid CreateModificationReqRequest request) {
        CreateModificationReqResponse response = modificationRequestService.createModificationRequest(request);
        APIResultResponse<CreateModificationReqResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Successfully created modification request",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @GetMapping("/out")
    @Schema(name = "GetOutModificationRequestRequest", description = "Get Out ModificationRequest request body")
    @Operation(summary = "Endpoint to load the outgoing modification request list")
    public ResponseEntity<APIResultResponse<Page<OutModificationReqResponse>>> getOutModificationRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<OutModificationReqResponse> responses = modificationRequestService.getOutModificationRequest(pageable);
        APIResultResponse<Page<OutModificationReqResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded outgoing modification request list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/in")
    @Schema(name = "GetInModificationRequestRequest", description = "Get In ModificationRequest request body")
    @Operation(summary = "Endpoint to load the incoming modification request list")
    public ResponseEntity<APIResultResponse<Page<InModificationReqResponse>>> getInModificationRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<InModificationReqResponse> responses = modificationRequestService.getInModificationRequest(pageable);
        APIResultResponse<Page<InModificationReqResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the incoming modification request list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/copy-recipe")
    @Schema(name = "GetCopyRecipeRequest", description = "Get Copy Recipe request body")
    @Operation(summary = "Endpoint to load a list of copy recipes for modification requests")
    public ResponseEntity<APIResultResponse<Page<MyCopyRecipeResponse>>> getCopyRecipeRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<MyCopyRecipeResponse> responses = modificationRequestService.getCopyRecipe(pageable);
        APIResultResponse<Page<MyCopyRecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the recipe list with copy type",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/approve/{fromRecipeSlug}")
    @Schema(name = "ApproveModificationRequest", description = "Approve Modification request body")
    @Operation(summary = "Endpoint for approving modification request")
    public ResponseEntity<APIResponse> approveRequest(@PathVariable String fromRecipeSlug) {
        modificationRequestService.approveRequest(fromRecipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully approved the modification request"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/reject/{fromRecipeSlug}")
    @Schema(name = "RejectModificationRequest", description = "Reject Modification request body")
    @Operation(summary = "Endpoint for rejecting modification request")
    public ResponseEntity<APIResponse> rejectRequest(@PathVariable String fromRecipeSlug) {
        modificationRequestService.rejectRequest(fromRecipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully rejected the modification request"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
