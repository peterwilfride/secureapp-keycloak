package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.SetVinculoDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoResponse;
import com.keycloak.auth.secureapp.dto.TokenResponse;
import com.keycloak.auth.secureapp.repostitory.AuthRepository;
import com.keycloak.auth.secureapp.utils.TokenDecoder;
import com.keycloak.auth.secureapp.utils.TokenExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository repository;
    private final UsersService usersService;
    private final TokenExtractor tokenExtractor;

    public AuthService(AuthRepository repository, UsersService usersService, TokenExtractor tokenExtractor) {
        this.repository = repository;
        this.usersService = usersService;
        this.tokenExtractor = tokenExtractor;
    }

    //String mockusername = "maria";

    public ResponseEntity<LogoutDtoResponse> logout(LogoutDtoRequest logoutRequest) {
        String tokenStr = tokenExtractor.extractToken();
        TokenDecoder decodeToken = TokenDecoder.getDecoded(tokenStr);
        String username = decodeToken.preferred_username;

        // resetar um vinculo id basta passar valor -1 para ele
        usersService.setVinculoId(-1L, username);

        return repository.logout(logoutRequest);
    }

    public Optional<TokenResponse> setVinculo(SetVinculoDtoRequest setVinculoDtoRequest) {
        /* TODO: Validar se esta apto ou nao a gerar um conjunto de tokens
         * se vinculo_id esta ativo
         * se vinculo_id pertence ao username
         * se username com vinculo_id esta morto
         * */

        String tokenStr = tokenExtractor.extractToken();
        TokenDecoder decodeToken = TokenDecoder.getDecoded(tokenStr);
        String username = decodeToken.preferred_username;

        Long vinculoId = setVinculoDtoRequest.getVinculo_id();
        usersService.setVinculoId(vinculoId, username);

        return repository.setVinculo(setVinculoDtoRequest);
    }

    public void getVinculos() {
        // get vinculos
    }
}
