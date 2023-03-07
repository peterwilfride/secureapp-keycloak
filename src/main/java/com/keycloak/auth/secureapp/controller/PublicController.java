package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.model.*;
import com.keycloak.auth.secureapp.service.PublicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {
    private final PublicService service;

    @PostMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestBody LoginDTO loginDTO) {
        Optional<ResponseToken> tokenOpt = service.login(loginDTO);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/forgot_password")
    public void forgot_password(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        // TODO
    }

    @PostMapping("/reset_password")
    public void reset_password(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // TODO
    }
}
