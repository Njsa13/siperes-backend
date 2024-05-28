package com.siperes.siperes.controller;

import com.siperes.siperes.dto.response.base.APIResultResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.siperes.siperes.common.util.Constants.TestAuth.TEST_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = TEST_PATS, produces = "application/json")
@Tag(name = "Test Auth", description = "Test Auth API")
public class TestAuthController {

    @GetMapping("/user")
    public ResponseEntity<APIResultResponse<String>> getUserLoginMessage() {
        String message = "Anda Login Sebagai User";
        APIResultResponse<String> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil Mengambil Pesan",
                message
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<APIResultResponse<String>> getAdminLoginMessage() {
        String message = "Anda Login Sebagai Admin";
        APIResultResponse<String> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil Mengambil Pesan",
                message
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
