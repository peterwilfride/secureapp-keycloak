package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RolesKeycloakRepository {
    private final RestTemplate template;
    private final AuthRepository authRepository;

    public RolesKeycloakRepository(RestTemplate template, AuthRepository authRepository) {
        this.template = template;
        this.authRepository = authRepository;
    }

    public RolesResponse[] getAllRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<RolesResponse[]> roles = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles",
                HttpMethod.GET, httpEntity, RolesResponse[].class);

        return roles.getBody();
    }

    public RolesResponse[] getRolesByUsers(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        // TODO: se o id nao for um uuid, bad request

        ResponseEntity<RolesResponse[]> roles = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+id+"/role-mappings/realm",
                HttpMethod.GET, httpEntity, RolesResponse[].class);

        return roles.getBody();
    }

    public UserRepresentationalResponse[] getUsersByRole(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        RolesResponse role = findById(id);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles/"+role.getName()+"/users",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public RolesResponse findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<RolesResponse> role = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles-by-id/"+id,
                HttpMethod.GET, httpEntity, RolesResponse.class);

        return role.getBody();
    }
}
