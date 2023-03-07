package com.keycloak.auth.secureapp.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class UserRepresentational {
    private UUID id;
    private List<CredentialRepresentation> credentials;
    private Map<String, List<String>> attributes;
    private String username; //TODO: Deve ser um cpf valido
    private String firstName;
    private String lastName;
    private boolean emailVerified;
    private String email;
    private boolean enabled;
    private long createdTimestamp;
}
