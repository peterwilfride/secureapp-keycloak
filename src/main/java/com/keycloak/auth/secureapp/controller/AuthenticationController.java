package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.model.ForgotPasswordRequest;
import com.keycloak.auth.secureapp.model.LogoutRequest;
import com.keycloak.auth.secureapp.model.ResetPasswordRequest;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.service.UserKeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final UserKeycloakService service;
    @PostMapping("/login")
    public ResponseEntity<ResponseToken> signin(@RequestBody LoginDTO loginDTO) {
        Optional<ResponseToken> tokenOpt = service.signin(loginDTO);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        // TODO
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
