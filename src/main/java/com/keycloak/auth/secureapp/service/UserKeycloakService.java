package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.ChooseVinculoDTO;
import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.dto.UserDTO;
import com.keycloak.auth.secureapp.model.*;
import com.keycloak.auth.secureapp.repostitory.UserKeycloakRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserKeycloakService {
    private final UserKeycloakRepository repository;

    public Boolean singup(UserDTO userDTO) {
        // check if username already exists
        Optional<UserRepresentational> user_email = repository.findByEmail(userDTO.getEmail());
        Optional<UserRepresentational> user_username = repository.findByUsernmae(userDTO.getUsername());

        if (user_email.isPresent() || user_username.isPresent()) {
            return false;
        }

        //TODO: checae se username e um cpf valido
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
        //attributes.put("reset_token", List.of("_"));
        attributes.put("vinculo_id", List.of("-1"));
        //attributes.put("reset_token_expite_in", List.of("_"));
        myUserRepresentational.setAttributes(attributes);

        HttpStatusCode res = repository.create(myUserRepresentational);

        if (res.is2xxSuccessful()) {
            return true;
        }
        return false;
    }

    public Optional<ResponseToken> signin(LoginDTO loginDTO) {
        return repository.authenticate(loginDTO);
    }

    public Optional<ResponseToken> chooseVinculo(ChooseVinculoDTO chooseVinculoDTO) {
        return repository.chooseVinculo(chooseVinculoDTO);
    }

    public Optional<UserRepresentational> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserRepresentationalResponse[] findAll() {
        return repository.findAll();
    }

    public Optional<UserRepresentationalResponse> findById(UUID id) {
        return repository.findById(id);
    }

    public UserRepresentational update(UUID userId, UserRepresentational newuser) {
        return repository.update(userId, newuser);
    }

    public String addRolByUserId(UUID user_id, UUID role_id) {
        return repository.addRoleByUserId(user_id, role_id);
    }
    public String removeRolByUserId(UUID user_id, UUID role_id) {
        return repository.removeRoleByUserId(user_id, role_id);
    }

    public String addGroupByUserId(UUID userId, UUID groupId) {
        return repository.addGroupByUserId(userId, groupId);
    }

    public String removeGroupByUserId(UUID userId, UUID groupId) {
        return repository.removeGroupByUserId(userId, groupId);
    }
}
