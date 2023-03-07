package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.UserDTO;
import com.keycloak.auth.secureapp.model.*;
import com.keycloak.auth.secureapp.service.UsersService;
import com.keycloak.auth.secureapp.utils.ValidatorUUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserController {
    private final UsersService service;
    private final ValidatorUUID validatorUUID;

    @GetMapping
    public UserRepresentationalResponse[] getAllUsers() {
        return service.findAll();
    }

    @GetMapping(params = {"id"})
    public ResponseEntity<UserRepresentational> findById(@RequestParam UUID id) {
        Optional<UserRepresentational> user = service.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(params = {"username"})
    public ResponseEntity<UserRepresentational> findByUsername(@RequestParam String username) {
        Optional<UserRepresentational> user = service.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(params = {"email"})
    public ResponseEntity<UserRepresentational> findByEmail(@RequestParam String email) {
        Optional<UserRepresentational> user = service.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = {"/{id}"})
    public void update(@PathVariable UUID id, @RequestBody UserRepresentational user) {
        //Optional<UserRepresentational> result = service.update(id, user);
        service.update(id, user);
        /*if (result.isPresent()) {
            return ResponseEntity.ok().body(result.get());
        }
        return ResponseEntity.notFound().build();*/
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody UserDTO userDTO) {
        Boolean user = service.register(userDTO);
        if (user) {
            return ResponseEntity.status(201).body(true);
        }
        return  ResponseEntity.status(409).build();
    }

    // CRUD DE GROUPS
    @GetMapping("groups/{id}")
    public ResponseEntity<GroupRepresentation[]> getGropsByUserId(@PathVariable UUID id) {
        if (!validatorUUID.validate(id)) {
            ResponseEntity.badRequest().build();
        }

        Optional<GroupRepresentation[]> groups = service.getGroupsByUserId(id);
        if (groups.isPresent()) {
            return ResponseEntity.ok(groups.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/{user_id}/groups/{group_id}")
    public ResponseEntity<Void> addGroupByUserId(@PathVariable UUID user_id, @PathVariable UUID group_id) {
        if (!validatorUUID.validate(user_id)) {
            ResponseEntity.badRequest().build();
        }
        if (!validatorUUID.validate(group_id)) {
            ResponseEntity.badRequest().build();
        }
        if (service.addGroupByUserId(user_id, group_id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{user_id}/groups/{group_id}")
    public ResponseEntity<Void> removeGroupByUserId(@PathVariable UUID user_id, @PathVariable UUID group_id) {
        if (!validatorUUID.validate(user_id)) {
            ResponseEntity.badRequest().build();
        }
        if (!validatorUUID.validate(group_id)) {
            ResponseEntity.badRequest().build();
        }
        if (service.removeGroupByUserId(user_id, group_id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // CRUD DE ROLES

    @GetMapping(path = "roles/{id}")
    public ResponseEntity<RolesResponse[]> getRolesByUserId(@PathVariable UUID id) {
        if (!validatorUUID.validate(id)) {
            ResponseEntity.badRequest().build();
        }

        Optional<RolesResponse[]> roles = service.getRolesByUserId(id);
        if (roles.isPresent()) {
            return ResponseEntity.ok(roles.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{user_id}/roles/{role_id}")
    public ResponseEntity<Void> addRoleByUserId(@PathVariable UUID user_id, @PathVariable UUID role_id) {
        if (!validatorUUID.validate(user_id)) {
            ResponseEntity.badRequest().build();
        }
        if (!validatorUUID.validate(role_id)) {
            ResponseEntity.badRequest().build();
        }
        if (service.addRoleByUserId(user_id, role_id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{user_id}/roles/{role_id}")
    public ResponseEntity<Void> removeRolesByUserId(@PathVariable UUID user_id, @PathVariable UUID role_id) {
        if (!validatorUUID.validate(user_id)) {
            ResponseEntity.badRequest().build();
        }
        if (!validatorUUID.validate(role_id)) {
            ResponseEntity.badRequest().build();
        }
        if (service.removeRoleByUserId(user_id, role_id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
