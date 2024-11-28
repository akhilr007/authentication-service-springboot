package com.akhil.expensetracker.authservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SuccessfulSignupDTO {

    private String name;
    private String email;
    private Set<UserRoleDTO> roles;
}
