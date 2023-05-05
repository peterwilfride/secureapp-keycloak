package com.keycloak.auth.secureapp.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LogoutDtoRequest {
    private String refresh_token;
}
