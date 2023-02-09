package com.keycloak.auth.secureapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
