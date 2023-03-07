package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class RolesRepository {
    private final RestTemplate template;
    private final AdminRepository adminRepository;

    public RolesRepository(RestTemplate template, AdminRepository adminRepository) {
        this.template = template;
        this.adminRepository = adminRepository;
    }

    public RolesResponse[] getAllRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<RolesResponse[]> roles = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles",
                HttpMethod.GET, httpEntity, RolesResponse[].class);

        return roles.getBody();
    }

    public UserRepresentationalResponse[] getUsersByRole(String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles/" + roleName + "/users",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public Optional<RolesResponse> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RolesResponse> role = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/roles-by-id/"+id,
                HttpMethod.GET, httpEntity, RolesResponse.class);
            return Optional.ofNullable(role.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
