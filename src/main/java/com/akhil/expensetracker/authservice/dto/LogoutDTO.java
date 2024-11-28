package com.akhil.expensetracker.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutDTO {

    private String message;
    private Boolean success;
}
