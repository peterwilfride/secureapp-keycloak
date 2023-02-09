package com.keycloak.auth.secureapp.model;

import lombok.Data;

import java.util.UUID;

@Data
public class RolesResponse {
    private UUID id;
    private String name;
}
