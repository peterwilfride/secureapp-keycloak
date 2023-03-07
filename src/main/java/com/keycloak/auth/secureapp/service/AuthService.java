package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.ChooseVinculoDTO;
import com.keycloak.auth.secureapp.model.LogoutRequest;
import com.keycloak.auth.secureapp.model.LogoutResponse;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.repostitory.AuthRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository repository;
    private final UsersService usersService;

    public AuthService(AuthRepository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }
    String mockusername = "maria";
    public ResponseEntity<LogoutResponse> logout(LogoutRequest logoutRequest) {
        //TODO: a partir do token recuperar o user e resetar seu vinculo_id

        // resetar um vinculo id basta passar valor -1 para ele
        usersService.setVinculoId(-1L, mockusername);

        return repository.logout(logoutRequest);
    }

    public Optional<ResponseToken> setVinculo(ChooseVinculoDTO chooseVinculoDTO) {
        /* TODO: a partir do token recuperar o vinculo_id do user
         * TODO: e validar se esta apto ou nao a gerar um conjunto de tokens
         * se vinculo_id esta ativo
         * se vinculo_id pertence ao username
         * se username com vinculo_id esta morto
         * */

        Long vinculoId = chooseVinculoDTO.getVinculo_id();
        usersService.setVinculoId(vinculoId, mockusername);

        return repository.setVinculo(chooseVinculoDTO);
    }
}
