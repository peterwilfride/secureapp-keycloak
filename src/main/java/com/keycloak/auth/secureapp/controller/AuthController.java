package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.dto.SetVinculoDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoResponse;
import com.keycloak.auth.secureapp.dto.TokenResponse;
import com.keycloak.auth.secureapp.service.AuthService;
import com.keycloak.auth.secureapp.utils.TokenDecoder;
import com.keycloak.auth.secureapp.utils.TokenExtractor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/admin/")
@AllArgsConstructor
public class AuthController {

    public final AuthService service;
    public final TokenExtractor tokenExtractor;

    @PostMapping("/logout")
    public ResponseEntity<LogoutDtoResponse> logout(@RequestBody LogoutDtoRequest logoutRequest) {
        return service.logout(logoutRequest);
    }

    @GetMapping("/vinculos")
    public void getVinculos() {
        service.getVinculos();
    }

    @PostMapping("/vinculos")
    public ResponseEntity<TokenResponse> setVinculo(@RequestBody SetVinculoDtoRequest setVinculoDtoRequest) {
        Optional<TokenResponse> tokenOpt = service.setVinculo(setVinculoDtoRequest);
        if (tokenOpt.isPresent()) {
            return ResponseEntity.ok().body(tokenOpt.get());
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping
    public void hello() {
        String tokenStr = tokenExtractor.extractToken();

        TokenDecoder decodeToken = TokenDecoder.getDecoded(tokenStr);

        String username = decodeToken.preferred_username;
        System.out.println(username);

        String groups = Arrays.toString(decodeToken.groups);
        System.out.println(groups);

        String vinculoId = decodeToken.vinculo_id.toString();
        System.out.println(vinculoId);
    }
}
