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
    @Operation(summary = "Endpoint untuk membuat modification request")
    public ResponseEntity<APIResultResponse<CreateModificationReqResponse>> createModificationRequest(@RequestBody @Valid CreateModificationReqRequest request) {
        CreateModificationReqResponse response = modificationRequestService.createModificationRequest(request);
        APIResultResponse<CreateModificationReqResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Berhasil membuat permintaan modifikasi",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @GetMapping("/out")
    @Schema(name = "GetOutModificationRequestRequest", description = "Get Out ModificationRequest request body")
    @Operation(summary = "Endpoint untuk menampilkan modification request yang keluar")
    public ResponseEntity<APIResultResponse<Page<OutModificationReqResponse>>> getOutModificationRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<OutModificationReqResponse> responses = modificationRequestService.getOutModificationRequest(pageable);
        APIResultResponse<Page<OutModificationReqResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat permintaan modifikasi yang keluar",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/in")
    @Schema(name = "GetInModificationRequestRequest", description = "Get In ModificationRequest request body")
    @Operation(summary = "Endpoint untuk menampilkan modification request yang masuk")
    public ResponseEntity<APIResultResponse<Page<InModificationReqResponse>>> getInModificationRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<InModificationReqResponse> responses = modificationRequestService.getInModificationRequest(pageable);
        APIResultResponse<Page<InModificationReqResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat permintaan modifikasi yang masuk",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/copy-recipe")
    @Schema(name = "GetCopyRecipeRequest", description = "Get Copy Recipe request body")
    @Operation(summary = "Endpoint untuk menampilkan daftar resep copy untuk modification request")
    public ResponseEntity<APIResultResponse<Page<MyCopyRecipeResponse>>> getCopyRecipeRequest(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<MyCopyRecipeResponse> responses = modificationRequestService.getCopyRecipe(pageable);
        APIResultResponse<Page<MyCopyRecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar resep dengan tipe copy",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/approve/{fromRecipeSlug}")
    @Schema(name = "ApproveModificationRequest", description = "Approve Modification request body")
    @Operation(summary = "Endpoint untuk menyetujui modifikasi request")
    public ResponseEntity<APIResponse> approveRequest(@PathVariable String fromRecipeSlug) {
        modificationRequestService.approveRequest(fromRecipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menyetujui permintaan modifikasi"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/reject/{fromRecipeSlug}")
    @Schema(name = "RejectModificationRequest", description = "Reject Modification request body")
    @Operation(summary = "Endpoint untuk menyetujui modifikasi request")
    public ResponseEntity<APIResponse> rejectRequest(@PathVariable String fromRecipeSlug) {
        modificationRequestService.rejectRequest(fromRecipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menolak permintaan modifikasi"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
