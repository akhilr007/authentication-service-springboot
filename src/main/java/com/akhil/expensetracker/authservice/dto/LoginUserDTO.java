package com.akhil.expensetracker.authservice.dto;

import com.akhil.expensetracker.authservice.entities.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class LoginUserDTO {

    private String email;
    private String password;
}
