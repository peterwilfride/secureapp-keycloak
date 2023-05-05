package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.RoleDtoResponse;
import com.keycloak.auth.secureapp.dto.UserRepresentationalResponse;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mykeycloak.base-url}")
    private String base_url;

    public RoleDtoResponse[] getAllRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<RoleDtoResponse[]> roles = template.exchange(
                base_url + "/auth/admin/realms/master/roles",
                HttpMethod.GET, httpEntity, RoleDtoResponse[].class);

        return roles.getBody();
    }

    public UserRepresentationalResponse[] getUsersByRole(String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                base_url + "/auth/admin/realms/master/roles/" + roleName + "/users",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public Optional<RoleDtoResponse> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<RoleDtoResponse> role = template.exchange(
                base_url + "/auth/admin/realms/master/roles-by-id/" + id,
                HttpMethod.GET, httpEntity, RoleDtoResponse.class);
            return Optional.ofNullable(role.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
