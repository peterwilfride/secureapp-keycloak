package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.service.RolesKeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/roles")
@AllArgsConstructor
public class RolesController {
    private final RolesKeycloakService service;

    @GetMapping
    public RolesResponse[] getAllRoles() {
        return service.getAllRoles();
    }

    @GetMapping("{id}/users")
    public UserRepresentationalResponse[] getUsersByRole(@PathVariable UUID id) {
        return service.getUsersByRole(id);
    }

    @GetMapping(path = "users/{id}")
    public RolesResponse[] getRolesByUserId(@PathVariable UUID id) {
        return service.getRolesByUser(id);
    }
}
