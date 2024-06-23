package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siperes.siperes.enumeration.EnumStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse {
    private EnumStatus status;
}
