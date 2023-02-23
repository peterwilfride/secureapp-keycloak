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

    public UserRepresentationalResponse convert() {
        UserRepresentationalResponse userRepresentationalResponse = new UserRepresentationalResponse();
        userRepresentationalResponse.setId(this.id);
        userRepresentationalResponse.setUsername(this.username);
        userRepresentationalResponse.setFirstName(this.firstName);
        userRepresentationalResponse.setLastName(this.lastName);
        userRepresentationalResponse.setEmail(this.email);
        userRepresentationalResponse.setCreatedTimestamp(this.createdTimestamp);
        return userRepresentationalResponse;
    }
}
