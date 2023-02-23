package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.repostitory.RolesKeycloakRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RolesKeycloakService {
    private final RolesKeycloakRepository rolesRepository;

    /*public RolesResponse findById(UUID id) {
        return rolesRepository.findById(id);
    }*/

    public RolesResponse[] getAllRoles() {
        return rolesRepository.getAllRoles();
    }

    public RolesResponse[] getRolesByUser(UUID id) {
        return rolesRepository.getRolesByUsers(id);
    }

    public UserRepresentationalResponse[] getUsersByRole(UUID id) {
        return rolesRepository.getUsersByRole(id);
    }
}
