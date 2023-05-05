package com.keycloak.auth.secureapp.dto;

import com.keycloak.auth.secureapp.model.CredentialRepresentation;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class UserRepresentationalRequest {
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
        UserRepresentationalResponse user = new UserRepresentationalResponse();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setEmail(this.getEmail());
        user.setCreatedTimestamp(this.getCreatedTimestamp());
        return user;
    }
}
