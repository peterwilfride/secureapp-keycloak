package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.*;
import com.keycloak.auth.secureapp.model.*;
import com.keycloak.auth.secureapp.repostitory.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final RolesService rolesService;
    private final GroupsService groupsService;

    public Boolean register(UserDtoRequest userDTO) {
        // check if username already exists
        Optional<UserRepresentationalRequest> user_email = repository.findByEmail(userDTO.getEmail());
        Optional<UserRepresentationalRequest> user_username = repository.findByUsername(userDTO.getUsername());

        if (user_email.isPresent() || user_username.isPresent()) {
            return false;
        }

        //TODO: VALIDAÇOES PARA REGISTRO DE USUARIO
        //TODO: checar se username e um cpf valido
        //TODO: na pagrn api checar se o cpf utilizado pertente a uma pessoa fisica

        // User data
        UserRepresentationalRequest myUserRepresentational = new UserRepresentationalRequest();
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
        attributes.put("vinculo_id", List.of("-1"));
        myUserRepresentational.setAttributes(attributes);

        HttpStatusCode res = repository.create(myUserRepresentational);

        return res.is2xxSuccessful();
    }

    public UserRepresentationalResponse[] findAll() {
        return repository.findAll();
    }

    public Optional<UserRepresentationalResponse> findById(UUID id) {
        Optional<UserRepresentationalRequest> userOpt = repository.findById(id);

        if (userOpt.isPresent()) {
            UserRepresentationalRequest user = userOpt.get();
            UserRepresentationalResponse userResponse = user.convert();
            return Optional.ofNullable(userResponse);
        }
        return Optional.empty();
    }

    public Optional<UserRepresentationalResponse> findByEmail(String email) {
        Optional<UserRepresentationalRequest> userOpt = repository.findByEmail(email);

        if (userOpt.isPresent()) {
            UserRepresentationalRequest user = userOpt.get();
            UserRepresentationalResponse userResponse = user.convert();
            return Optional.ofNullable(userResponse);
        }
        return Optional.empty();
    }

    public Optional<UserRepresentationalResponse> findByUsername(String username) {
        Optional<UserRepresentationalRequest> userOpt = repository.findByUsername(username);

        if (userOpt.isPresent()) {
            UserRepresentationalRequest user = userOpt.get();
            UserRepresentationalResponse userResponse = user.convert();
            return Optional.ofNullable(userResponse);
        }
        return Optional.empty();
    }

    public void update(UUID id, UserRepresentationalRequest user) {
        // FIXME: os serviços que usam findById vao usar diretamente do repositorio
        Optional<UserRepresentationalRequest> userOpt = repository.findById(id);

        if (userOpt.isPresent()) {
            repository.update(id, user);
        }
    }

    public void setVinculoId(Long vinculoId, String mockusername) {
        Optional<UserRepresentationalRequest> userOpt = repository.findByUsername(mockusername);
        if (userOpt.isPresent()) {
            UserRepresentationalRequest user = userOpt.get();
            //user.setVinculoId(vinculoId);
            Map<String, List<String>> attr = user.getAttributes();
            attr.replace("vinculo_id", List.of(vinculoId.toString()));
            repository.update(user.getId(), user);
        }
    }

    public Boolean addRoleByUserId(UUID userId, UUID roleId) {
        Optional<UserRepresentationalRequest> user = repository.findById(userId);
        Optional<RoleDtoResponse> role = rolesService.findById(roleId);

        if (user.isPresent() && role.isPresent()){
            RoleDtoResponse[] roles = {role.get()};
            return repository.addRoleByUserId(userId, roles);
        }
        return false;
    }

    public Boolean removeRoleByUserId(UUID userId, UUID roleId) {
        Optional<UserRepresentationalRequest> user = repository.findById(userId);
        Optional<RoleDtoResponse> role = rolesService.findById(roleId);

        if (user.isPresent() && role.isPresent()){
            RoleDtoResponse[] roles = {role.get()};
            return repository.removeRoleByUserId(userId, roles);
        }
        return false;
    }

    public Optional<RoleDtoResponse[]> getRolesByUserId(UUID userId) {
        Optional<UserRepresentationalRequest> user = repository.findById(userId);

        if (user.isPresent()) {
            RoleDtoResponse[] roles = repository.getRolesByUserId(userId);
            RoleDtoResponse[] rolesFiltered = rolesService.filter_from_blacklist(roles);
            return Optional.ofNullable(rolesFiltered);
        }

        return Optional.empty();
    }

    public Boolean addGroupByUserId(UUID userId, UUID groupId) {
        Optional<UserRepresentationalRequest> user = repository.findById(userId);
        Optional<GroupDtoResponse> group = groupsService.findById(groupId);

        if (user.isPresent() && group.isPresent()) {
            return repository.addGroupByUserId(userId, groupId);
        }
        return false;
    }

    public Boolean removeGroupByUserId(UUID userId, UUID groupId) {
        Optional<UserRepresentationalRequest> user = repository.findById(userId);
        Optional<GroupDtoResponse> group = groupsService.findById(groupId);

        if (user.isPresent() && group.isPresent()) {
            return repository.removeGroupByUserId(userId, groupId);
        }
        return false;
    }

    public Optional<GroupDtoResponse[]> getGroupsByUserId(UUID id) {
        Optional<UserRepresentationalRequest> user = repository.findById(id);

        if (user.isPresent()) {
            GroupDtoResponse[] groups = repository.getGroupsByUserId(id);
            return Optional.ofNullable(groups);
        }
        return Optional.empty();
    }
}
