package com.keycloak.auth.secureapp.model;

import lombok.Data;

@Data
public class UserRepresentationalResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private String email;
    private boolean enabled;
    private long createdTimestamp;
}
