package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.repostitory.RolesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RolesService {
    private final RolesRepository repository;
    private final String[] BLACK_LIST_ROLES = {"offline_access","default-roles-master","uma_authorization","create-realm","admin"};
    // caso apareça uma nova role para escoder, basta adicionar ela aqui

    public RolesResponse[] getAllRoles() {
        RolesResponse[] roles =  repository.getAllRoles();
        return filter_from_blacklist(roles);
    }

    public Optional<UserRepresentationalResponse[]> getUsersByRoleId(UUID id) {
        Optional<RolesResponse> role = repository.findById(id);
        if (role.isPresent()) {
            String rolename = role.get().getName();
            UserRepresentationalResponse[] users = repository.getUsersByRole(rolename);
            return Optional.ofNullable(users);
        }
        return Optional.empty();
    }

    public Optional<RolesResponse> findById(UUID id) {
        return repository.findById(id);
    }

    public RolesResponse[] filter_from_blacklist(RolesResponse[] array) {
        int i, j;
        for(i = 0, j = 0; j < array.length; j++) {
            if(!Arrays.asList(BLACK_LIST_ROLES).contains(array[j].getName())) {
                array[i++] = array[j];
            }
        }
        return Arrays.copyOf(array, i);
    }
}
