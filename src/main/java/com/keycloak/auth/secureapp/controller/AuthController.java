package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.ChooseVinculoDTO;
import com.keycloak.auth.secureapp.model.LogoutRequest;
import com.keycloak.auth.secureapp.model.LogoutResponse;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/admin/")
@AllArgsConstructor
public class AuthController {
    public final AuthService service;
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest logoutRequest) {
        return service.logout(logoutRequest);
    }
    @PostMapping("/vinculos")
    public ResponseEntity<ResponseToken> setVinculo(@RequestBody ChooseVinculoDTO chooseVinculoDTO) {
        Optional<ResponseToken> tokenOpt = service.setVinculo(chooseVinculoDTO);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }
}
