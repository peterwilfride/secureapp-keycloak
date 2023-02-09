package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.dto.UserDTO;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {
    private final KeycloakService service;

    @PostMapping("signup")
    public ResponseEntity<UserRepresentational> signup(@RequestBody UserDTO userDTO) {
        UserRepresentational user = service.singup(userDTO);
        if (user == null) { return  ResponseEntity.status(409).build(); }
        return ResponseEntity.status(201).body(user);
    }

    @PostMapping("signin")
    public ResponseEntity<ResponseToken> signin(@RequestBody LoginDTO loginDTO) {
        Optional<ResponseToken> tokenOpt = service.signin(loginDTO);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("findbyemail")
    public ResponseEntity<UserRepresentational> findByEmail(@RequestParam String email) {
        Optional<UserRepresentational> user = service.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<UserRepresentational> getOne(@PathVariable UUID id) {
        Optional<UserRepresentational> user = service.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<UserRepresentational> update(@PathVariable UUID id, @RequestBody UserRepresentational newuser) {
        Optional<UserRepresentational> user = service.findById(id);
        if (user.isPresent()) {
            UserRepresentational u = service.update(id, newuser);
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }
}
