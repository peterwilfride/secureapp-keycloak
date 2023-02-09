package com.keycloak.auth.secureapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String username;
    private String password;
    private Long vinculoId;
}
