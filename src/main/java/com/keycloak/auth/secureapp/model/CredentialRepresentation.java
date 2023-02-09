package com.keycloak.auth.secureapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialRepresentation {
    private String type;
    private String value;
    private Boolean temporary;
}
