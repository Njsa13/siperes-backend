package com.siperes.siperes.dto.response.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    protected Date timestamp;

    protected String status;

    protected int code;

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