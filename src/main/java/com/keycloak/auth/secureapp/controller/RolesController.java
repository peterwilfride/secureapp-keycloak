package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.service.RolesService;
import com.keycloak.auth.secureapp.utils.ValidatorUUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/roles")
@AllArgsConstructor
public class RolesController {
    private final RolesService service;
    private final ValidatorUUID validatorUUID;

    @GetMapping
    public RolesResponse[] getAllRoles() {
        return service.getAllRoles();
    }

    @GetMapping("{id}/users")
    public ResponseEntity<UserRepresentationalResponse[]> getUsersByRoleId(@PathVariable UUID id) {
        if (!validatorUUID.validate(id)) {
            ResponseEntity.badRequest().build();
        }

        Optional<UserRepresentationalResponse[]> users = service.getUsersByRoleId(id);
        if (users.isPresent()) {
            return ResponseEntity.ok(users.get());
        }
        return ResponseEntity.notFound().build();
    }
}
