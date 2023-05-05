package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.LoginDtoRequest;
import com.keycloak.auth.secureapp.dto.TokenResponse;
import com.keycloak.auth.secureapp.repostitory.PublicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicService {
    private final PublicRepository repository;
    public Optional<TokenResponse> login(LoginDtoRequest loginDTO) {
        return repository.authenticate(loginDTO);
    }
}
