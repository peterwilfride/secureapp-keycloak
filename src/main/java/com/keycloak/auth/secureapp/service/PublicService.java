package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.repostitory.PublicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PublicService {
    private final PublicRepository repository;
    public Optional<ResponseToken> login(LoginDTO loginDTO) {
        return repository.authenticate(loginDTO);
    }
}
