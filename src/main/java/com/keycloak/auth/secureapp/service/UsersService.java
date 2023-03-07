package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.UserDTO;
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

    public Boolean register(UserDTO userDTO) {
        // check if username already exists
        Optional<UserRepresentational> user_email = repository.findByEmail(userDTO.getEmail());
        Optional<UserRepresentational> user_username = repository.findByUsername(userDTO.getUsername());

        if (user_email.isPresent() || user_username.isPresent()) {
            return false;
        }

        //TODO: VALIDAÇOES PARA REGISTRO DE USUARIO
        //TODO: checar se username e um cpf valido
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
        attributes.put("vinculo_id", List.of("-1"));
        myUserRepresentational.setAttributes(attributes);

        HttpStatusCode res = repository.create(myUserRepresentational);

        return res.is2xxSuccessful();
    }

    public UserRepresentationalResponse[] findAll() {
        return repository.findAll();
    }

    public Optional<UserRepresentational> findById(UUID id) {
        // TODO: metodo para converter UserRepresentational para UserRepresentationalResponse para os endpoints
        return repository.findById(id);
    }

    public Optional<UserRepresentational> findByEmail(String email) {
        // TODO: metodo para converter UserRepresentational para UserRepresentationalResponse para os endpoints
        return repository.findByEmail(email);
    }

    public Optional<UserRepresentational> findByUsername(String username) {
        // TODO: metodo para converter UserRepresentational para UserRepresentationalResponse para os endpoints
        return repository.findByUsername(username);
    }

    public void update(UUID id, UserRepresentational user) {
        // FIXME: os serviços que usam findById vao usar diretamente do repositorio
        Optional<UserRepresentational> userOpt = repository.findById(id);

        if (userOpt.isPresent()) {
            repository.update(id, user);
        }
    }

    public void setVinculoId(Long vinculoId, String mockusername) {
        Optional<UserRepresentational> userOpt = repository.findByUsername(mockusername);
        if (userOpt.isPresent()) {
            UserRepresentational user = userOpt.get();
            //user.setVinculoId(vinculoId);
            Map<String, List<String>> attr = user.getAttributes();
            attr.replace("vinculo_id", List.of(vinculoId.toString()));
            repository.update(user.getId(), user);
        }
    }

    public Boolean addRoleByUserId(UUID userId, UUID roleId) {
        Optional<UserRepresentational> user = repository.findById(userId);
        Optional<RolesResponse> role = rolesService.findById(roleId);

        if (user.isPresent() && role.isPresent()){
            RolesResponse[] roles = {role.get()};
            return repository.addRoleByUserId(userId, roles);
        }
        return false;
    }

    public Boolean removeRoleByUserId(UUID userId, UUID roleId) {
        Optional<UserRepresentational> user = repository.findById(userId);
        Optional<RolesResponse> role = rolesService.findById(roleId);

        if (user.isPresent() && role.isPresent()){
            RolesResponse[] roles = {role.get()};
            return repository.removeRoleByUserId(userId, roles);
        }
        return false;
    }

    public Optional<RolesResponse[]> getRolesByUserId(UUID userId) {
        Optional<UserRepresentational> user = repository.findById(userId);

        if (user.isPresent()) {
            RolesResponse[] roles = repository.getRolesByUserId(userId);
            RolesResponse[] rolesFiltered = rolesService.filter_from_blacklist(roles);
            return Optional.ofNullable(rolesFiltered);
        }

        return Optional.empty();
    }

    public Boolean addGroupByUserId(UUID userId, UUID groupId) {
        Optional<UserRepresentational> user = repository.findById(userId);
        Optional<GroupRepresentation> group = groupsService.findById(groupId);

        if (user.isPresent() && group.isPresent()) {
            return repository.addGroupByUserId(userId, groupId);
        }
        return false;
    }

    public Boolean removeGroupByUserId(UUID userId, UUID groupId) {
        Optional<UserRepresentational> user = repository.findById(userId);
        Optional<GroupRepresentation> group = groupsService.findById(groupId);

        if (user.isPresent() && group.isPresent()) {
            return repository.removeGroupByUserId(userId, groupId);
        }
        return false;
    }

    public Optional<GroupRepresentation[]> getGroupsByUserId(UUID id) {
        Optional<UserRepresentational> user = repository.findById(id);

        if (user.isPresent()) {
            GroupRepresentation[] groups = repository.getGroupsByUserId(id);
            return Optional.ofNullable(groups);
        }
        return Optional.empty();
    }
}
