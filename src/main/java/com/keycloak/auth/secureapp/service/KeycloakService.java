package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.dto.UserDTO;
import com.keycloak.auth.secureapp.model.CredentialRepresentation;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.repostitory.KeycloakRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class KeycloakService {
    private final KeycloakRepository repository;

    public UserRepresentational singup(UserDTO userDTO) {
        // check if username already exists
        Optional<UserRepresentational> user_email = repository.findByEmail(userDTO.getEmail());
        Optional<UserRepresentational> user_username = repository.findByUsernmae(userDTO.getUsername());

        if (user_email.isPresent() || user_username.isPresent()) {
            return null;
        }

        //TODO: na pagrn api checar se o cpf utilizado pertente a uma pessoa fisica

        // User data
        UserRepresentational myUserRepresentational = new UserRepresentational();
        myUserRepresentational.setUsername(userDTO.getUsername());
        myUserRepresentational.setEmail(userDTO.getEmail());
        myUserRepresentational.setEmailVerified(false);
        myUserRepresentational.setFirstName(userDTO.getFirstName());
        myUserRepresentational.setLastName(userDTO.getLastName());
        myUserRepresentational.setEnabled(true);

        // User credentials
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(userDTO.getPassword());
        credentialRepresentation.setTemporary(false);
        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        myUserRepresentational.setCredentials(credentials);

        // User attributes
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("reset_token", List.of("_"));
        attributes.put("vinculo_id", List.of("_"));
        attributes.put("reset_token_expite_in", List.of("_"));
        myUserRepresentational.setAttributes(attributes);

        return repository.create(myUserRepresentational);
    }

    public Optional<ResponseToken> signin(LoginDTO loginDTO) {
        return repository.authenticate(loginDTO);
    }

    public Optional<UserRepresentational> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<UserRepresentational> findById(UUID id) {
        return repository.findById(id);
    }

    public UserRepresentational update(UUID userId, UserRepresentational newuser) {
        return repository.update(userId, newuser);
    }

    public RolesResponse[] getRolesByUser(UUID id) {
        return repository.getRolesByUsers(id);
    }

    public UserRepresentational[] getUsersByRole(String roleName) {
        return repository.getUsersByRoles(roleName);
    }

    public String addRolByUserId(UUID id, RolesResponse[] rolesResponse) {
        return repository.addRoleByUserId(id, rolesResponse);
    }
}
