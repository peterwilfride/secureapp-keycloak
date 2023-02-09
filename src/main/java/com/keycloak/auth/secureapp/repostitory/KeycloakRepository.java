package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.LoginDTO;
import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.model.ResponseToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class KeycloakRepository {
    private final RestTemplate template;

    public KeycloakRepository(RestTemplate template) {
        this.template = template;
    }

    @Value("${mykeycloak.admin-client-id}")
    private String admin_client_id;
    @Value("${mykeycloak.admin-client-secret}")
    private String admin_client_secret;
    @Value("${mykeycloak.admin-grant-type}")
    private String admin_grant_type;
    @Value("${mykeycloak.client-id}")
    private String client_id;
    @Value("${mykeycloak.client-secret}")
    private String client_secret;
    @Value("${mykeycloak.grant-type}")
    private String grant_type;

    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> credentials = new LinkedMultiValueMap<>();
        credentials.add("client_id", admin_client_id);
        credentials.add("client_secret", admin_client_secret);
        credentials.add("grant_type", admin_grant_type);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(credentials, headers);
        ResponseEntity<ResponseToken> res = template.postForEntity(
                "http://localhost:28080/auth/realms/master/protocol/openid-connect/token",
                httpEntity, ResponseToken.class);

        return Objects.requireNonNull(res.getBody()).getAccess_token();
    }

    public Optional<ResponseToken> authenticate(LoginDTO loginDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("client_id", client_id);
        userCredentials.add("client_secret", client_secret);
        userCredentials.add("grant_type", grant_type);
        userCredentials.add("username", loginDTO.getUsername());
        userCredentials.add("password", loginDTO.getPassword());

        if (loginDTO.getVinculoId() != null) {
            // atualizar o atributo vinculo_id do usuario
            Optional<UserRepresentational> userOpt = findByUsernmae(loginDTO.getUsername());
            if (userOpt.isPresent()) {
                // altera o objeto vinculo_id
                UserRepresentational user = userOpt.get();
                Map<String, List<String>> attr = user.getAttributes();
                attr.replace("vinculo_id", List.of(loginDTO.getVinculoId().toString()));
                // salva o usuario
                update(user.getId(), user);
            }
        }

        // se user esta desabilitado -> 400 bad request
        // se user errou credenciais -> 401 unauthorize

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userCredentials, headers);

        try {
            ResponseEntity<ResponseToken> res = template.postForEntity("" +
                    "http://localhost:28080/auth/realms/master/protocol/openid-connect/token",
                    httpEntity, ResponseToken.class);
            return Optional.ofNullable(res.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public UserRepresentational create(UserRepresentational myUserRepresentational) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentational> request = new HttpEntity<>(myUserRepresentational, headers);

        ResponseEntity<UserRepresentational> user = template.postForEntity(
                "http://localhost:28080/auth/admin/realms/master/users", request, UserRepresentational.class);
        return user.getBody();
    }

    public UserRepresentational update(UUID userId, UserRepresentational myUserRepresentational) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentational> request = new HttpEntity<>(myUserRepresentational, headers);
        ResponseEntity<UserRepresentational> user = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+userId,
                HttpMethod.PUT, request, UserRepresentational.class);
        return user.getBody();
    }

    public Optional<UserRepresentational> findByEmail(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
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
        headers.setBearerAuth(getAdminToken());
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

    public Optional<UserRepresentational> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserRepresentational> user = template.exchange(
                    "http://localhost:28080/auth/admin/realms/master/users/" + id, HttpMethod.GET,
                    httpEntity, UserRepresentational.class);
            return Optional.ofNullable(user.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public RolesResponse[] getRolesByUsers(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        // TODO: se o id nao for um uuid, bad request

        ResponseEntity<RolesResponse[]> roles = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+id+"/role-mappings/realm",
                HttpMethod.GET, httpEntity, RolesResponse[].class);

        return roles.getBody();
    }

    public UserRepresentational[] getUsersByRoles(String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        //TODO: checar se rolename existe no sistema e talvez fazer busca por id

        ResponseEntity<UserRepresentational[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles/"+roleName+"/users",
                HttpMethod.GET, httpEntity, UserRepresentational[].class);

        return users.getBody();
    }

    public String addRoleByUserId(UUID id, RolesResponse[] rolesResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RolesResponse[]> httpEntity = new HttpEntity<>(rolesResponse, headers);

        ResponseEntity<Void> roles = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+id+"/role-mappings/realm",
                HttpMethod.POST, httpEntity, Void.class);

        if (roles.getStatusCode().is2xxSuccessful()) {
            return "Role atribuida com sucesso!";
        }
        return "Algo deu errado!";
    }
}
