package com.keycloak.auth.secureapp.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRepresentationalResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private String email;
    private boolean enabled;
    private long createdTimestamp;
}
