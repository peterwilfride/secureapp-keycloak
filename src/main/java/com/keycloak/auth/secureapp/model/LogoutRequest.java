package com.keycloak.auth.secureapp.model;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LogoutRequest {
    private String refresh_token;
}
