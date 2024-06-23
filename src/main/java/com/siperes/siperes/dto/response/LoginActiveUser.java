package com.siperes.siperes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginActiveUser {
    private Integer daily;
    private Integer weekly;
    private Integer monthly;
}
