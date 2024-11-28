package com.akhil.expensetracker.authservice.dto;

import com.akhil.expensetracker.authservice.enums.RoleType;
import lombok.Data;

@Data
public class UserRoleDTO {

    private RoleType roleType;

}
