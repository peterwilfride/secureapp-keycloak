package com.keycloak.auth.secureapp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleDtoResponse {
    private UUID id;
    private String name;
    private String description;
}
