package com.keycloak.auth.secureapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDtoRequest {
    private String username;
    private String password;
}
