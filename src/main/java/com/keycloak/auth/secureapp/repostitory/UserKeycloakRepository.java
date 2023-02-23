package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.dto.ChooseVinculoDTO;
import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.model.ResponseToken;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class UserKeycloakRepository {
    private final RestTemplate template;
    private final AuthRepository authRepository;
    private final RolesKeycloakRepository rolesRepository;

    public UserKeycloakRepository(RestTemplate template, AuthRepository authRepository, RolesKeycloakRepository rolesRepository) {
        this.template = template;
        this.authRepository = authRepository;
        this.rolesRepository = rolesRepository;
    }

    @Value("${mykeycloak.client-id}")
    private String client_id;
    @Value("${mykeycloak.client-secret}")
    private String client_secret;
    @Value("${mykeycloak.grant-type}")
    private String grant_type;

    public Optional<ResponseToken> authenticate(LoginDTO loginDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("client_id", client_id);
        userCredentials.add("client_secret", client_secret);
        userCredentials.add("grant_type", grant_type);
        userCredentials.add("username", loginDTO.getUsername());
        userCredentials.add("password", loginDTO.getPassword());

        // se user esta desabilitado -> 400 bad request
        // se user errou credenciais -> 401 unauthorize

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userCredentials, headers);

        try {
            ResponseEntity<ResponseToken> res = template.postForEntity("http://localhost:28080/auth/realms/master/protocol/openid-connect/token",
                    httpEntity, ResponseToken.class);
            return Optional.ofNullable(res.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }


    public Optional<ResponseToken> chooseVinculo(ChooseVinculoDTO chooseVinculoDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // recuperar username do usuario a partir do token
        String username = "maria";

        // TODO: aqui ja e bom checar se o vinculo passado esta dentro dos conformes para ganantir que o token gerado esteja com informaçoes corretas
        // se esta ativo
        // se pertence ao usuario

        Optional<UserRepresentational> userOpt = findByUsernmae(username);
        if (userOpt.isPresent()) {
            UserRepresentational user = userOpt.get();
            Map<String, List<String>> attr = user.getAttributes();
            attr.replace("vinculo_id", List.of(chooseVinculoDTO.getVinculo_id().toString()));
            update(user.getId(), user);
        }

        MultiValueMap<String, String> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("client_id", client_id);
        userCredentials.add("client_secret", client_secret);
        userCredentials.add("grant_type", "refresh_token");
        userCredentials.add("refresh_token", chooseVinculoDTO.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userCredentials, headers);

        try {
            ResponseEntity<ResponseToken> res = template.postForEntity("http://localhost:28080/auth/realms/master/protocol/openid-connect/token",
                    httpEntity, ResponseToken.class);
            return Optional.ofNullable(res.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public HttpStatusCode create(UserRepresentational userRepresentational) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentational> request = new HttpEntity<>(userRepresentational, headers);

        ResponseEntity<HttpStatusCode> res = template.exchange(
                    "http://localhost:28080/auth/admin/realms/master/users",
                    HttpMethod.POST, request, HttpStatusCode.class);

        return res.getStatusCode();
    }

    public UserRepresentationalResponse[] findAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RolesResponse[]> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange("http://localhost:28080/auth/admin/realms/master/users", HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }


    public UserRepresentational update(UUID userId, UserRepresentational newuser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentational> request = new HttpEntity<>(newuser, headers);
        ResponseEntity<UserRepresentational> user = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+userId,
                HttpMethod.PUT, request, UserRepresentational.class);
        return user.getBody();
    }

    public Optional<UserRepresentational> findByEmail(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentational[]> user = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users?exact=true&email="+email,
                HttpMethod.GET, httpEntity, UserRepresentational[].class);

        if (Arrays.stream(user.getBody()).toList().size() == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(Arrays.stream(user.getBody()).toList().get(0));
    }

    public Optional<UserRepresentational> findByUsernmae(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentational[]> user = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users?exact=true&username="+username,
                HttpMethod.GET, httpEntity, UserRepresentational[].class);

        if (Arrays.stream(user.getBody()).toList().size() == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(Arrays.stream(user.getBody()).toList().get(0));
    }

    public Optional<UserRepresentationalResponse> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserRepresentationalResponse> user = template.exchange(
                    "http://localhost:28080/auth/admin/realms/master/users/" + id, HttpMethod.GET,
                    httpEntity, UserRepresentationalResponse.class);
            return Optional.ofNullable(user.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public String addRoleByUserId(UUID user_id, UUID role_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        RolesResponse role = rolesRepository.findById(role_id);
        RolesResponse[] roles = { role };

        HttpEntity<RolesResponse[]> httpEntity = new HttpEntity<>(roles, headers);

        ResponseEntity<Void> res = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+user_id+"/role-mappings/realm",
                HttpMethod.POST, httpEntity, Void.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return "Role atribuida com sucesso!";
        }
        return "Algo deu errado!";
    }

    public String removeRoleByUserId(UUID user_id, UUID role_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        RolesResponse role = rolesRepository.findById(role_id);
        RolesResponse[] roles = { role };

        HttpEntity<RolesResponse[]> httpEntity = new HttpEntity<>(roles, headers);

        ResponseEntity<Void> res = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+user_id+"/role-mappings/realm",
                HttpMethod.DELETE, httpEntity, Void.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return "Role removida com sucesso!";
        }
        return "Algo deu errado!";
    }

    public String addGroupByUserId(UUID userId, UUID groupId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> res = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+userId+"/groups/"+groupId,
                HttpMethod.PUT, httpEntity, Void.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return "Grupo atribuido com sucesso!";
        }
        return "Algo deu errado!";
    }

    public String removeGroupByUserId(UUID userId, UUID groupId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> res = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+userId+"/groups/"+groupId,
                HttpMethod.DELETE, httpEntity, Void.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            return "Grupo removido com sucesso!";
        }
        return "Algo deu errado!";
    }
}
