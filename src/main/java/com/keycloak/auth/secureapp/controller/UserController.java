package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.ChooseVinculoDTO;
import com.keycloak.auth.secureapp.dto.UserDTO;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.service.UserKeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserController {
    private final UserKeycloakService service;

    @GetMapping
    public UserRepresentationalResponse[] getAll() {
        return service.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<UserRepresentationalResponse> getOne(@PathVariable UUID id) {
        Optional<UserRepresentationalResponse> user = service.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody UserDTO userDTO) {
        Boolean user = service.singup(userDTO);
        if (user) {
            return ResponseEntity.status(201).body(user);
        }
        return  ResponseEntity.status(409).build();
    }

    @PostMapping("/vinculos")
    public ResponseEntity<ResponseToken> chooseVinculo(@RequestBody ChooseVinculoDTO chooseVinculoDTO) {
        Optional<ResponseToken> tokenOpt = service.chooseVinculo(chooseVinculoDTO);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<UserRepresentational> update(@PathVariable UUID id, @RequestBody UserRepresentational newuser) {
        Optional<UserRepresentationalResponse> user = service.findById(id);
        if (user.isPresent()) {
            UserRepresentational u = service.update(id, newuser);
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{user_id}/groups/{group_id}")
    public ResponseEntity<String> addGroupByUserId(@PathVariable UUID user_id, @PathVariable UUID group_id) {
        Optional<UserRepresentationalResponse> user = service.findById(user_id);
        //TODO: buscar grupo por id
        if (user.isPresent()) {
            String res = service.addGroupByUserId(user_id, group_id);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{user_id}/groups/{group_id}")
    public ResponseEntity<String> removeGroupByUserId(@PathVariable UUID user_id, @PathVariable UUID group_id) {
        Optional<UserRepresentationalResponse> user = service.findById(user_id);
        //TODO: buscar grupo por id
        if (user.isPresent()) {
            String res = service.removeGroupByUserId(user_id, group_id);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{user_id}/roles/{role_id}")
    public String addRoleByUserId(@PathVariable UUID user_id, @PathVariable UUID role_id) {
        return service.addRolByUserId(user_id, role_id);
    }

    @DeleteMapping(path = "/{user_id}/roles/{role_id}")
    public String removeRolesByUserId(@PathVariable UUID user_id, @PathVariable UUID role_id) {
        return service.removeRolByUserId(user_id, role_id);
    }
}
