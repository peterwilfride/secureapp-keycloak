package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.service.GroupsService;
import com.keycloak.auth.secureapp.utils.ValidatorUUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/groups")
@AllArgsConstructor
public class GroupsController {

    public final GroupsService service;
    private final ValidatorUUID validatorUUID;

    @GetMapping
    public GroupRepresentation[] getAllGroups() {
        return service.getAllGroups();
    }

    @GetMapping("{id}/users")
    public ResponseEntity<UserRepresentationalResponse[]> getUsersByGroupId(@PathVariable UUID id) {
        if (!validatorUUID.validate(id)) {
            ResponseEntity.badRequest().build();
        }

        Optional<UserRepresentationalResponse[]> users = service.getUsersByGroupId(id);
        if (users.isPresent()) {
            return ResponseEntity.ok(users.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/roles")
    public void getRolesToGroupId(@PathVariable UUID id) {
        if (!validatorUUID.validate(id)) {
            ResponseEntity.badRequest().build();
        }

        // TODO:
    }

    @PutMapping("/{group_id}/roles/{role_id}")
    public void addRoleToGroupId(@PathVariable UUID group_id, @PathVariable UUID role_id) {
        if (!validatorUUID.validate(group_id)) {
            ResponseEntity.badRequest().build();
        }

        if (!validatorUUID.validate(role_id)) {
            ResponseEntity.badRequest().build();
        }

        //TODO
    }

    @DeleteMapping("/{group_id}/roles/{role_id}")
    public void removeRoleToGroupId(@PathVariable UUID group_id, @PathVariable UUID role_id) {
        if (!validatorUUID.validate(group_id)) {
            ResponseEntity.badRequest().build();
        }

        if (!validatorUUID.validate(role_id)) {
            ResponseEntity.badRequest().build();
        }
        // TODO:
    }
}
