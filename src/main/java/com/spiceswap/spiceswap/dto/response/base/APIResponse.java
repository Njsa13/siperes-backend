package com.spiceswap.spiceswap.dto.response.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Schema(description = "Timestamp of the response")
    protected Date timestamp;

    @Schema(description = "Status of the response")
    protected String status;

    @Schema(description = "HTTP status code of the response")
    protected int code;

    @Schema(description = "Message associated with the response")
    protected String message;

    public APIResponse() {
        this.timestamp = new Date();
    }

    public APIResponse(HttpStatus httpStatus, String message) {
        this.status = httpStatus.name();
        this.message = message;
        this.code = httpStatus.value();
    }
}