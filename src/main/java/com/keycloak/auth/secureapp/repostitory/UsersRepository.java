package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.GroupDtoResponse;
import com.keycloak.auth.secureapp.dto.RoleDtoResponse;
import com.keycloak.auth.secureapp.dto.UserRepresentationalRequest;
import com.keycloak.auth.secureapp.dto.UserRepresentationalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class UsersRepository {
    private final RestTemplate template;
    private final AdminRepository AdminRepository;

    public UsersRepository(RestTemplate template, AdminRepository AdminRepository) {
        this.template = template;
        this.AdminRepository = AdminRepository;
    }

    @Value("${mykeycloak.base-url}")
    private String base_url;

    public HttpStatusCode create(UserRepresentationalRequest userRepresentational) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentationalRequest> request = new HttpEntity<>(userRepresentational, headers);

        ResponseEntity<HttpStatusCode> res = template.exchange(
                base_url + "/admin/realms/master/users",
                    HttpMethod.POST, request, HttpStatusCode.class);

        return res.getStatusCode();
    }

    public void update(UUID userId, UserRepresentationalRequest newuser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRepresentationalRequest> request = new HttpEntity<>(newuser, headers);

        ResponseEntity<UserRepresentationalRequest> user = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + userId,
                HttpMethod.PUT, request, UserRepresentationalRequest.class);
    }

    public UserRepresentationalResponse[] findAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                base_url + "/admin/realms/master/users",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public Optional<UserRepresentationalRequest> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserRepresentationalRequest> user = template.exchange(
                    base_url + "/auth/admin/realms/master/users/" + id, HttpMethod.GET,
                    httpEntity, UserRepresentationalRequest.class);
            return Optional.ofNullable(user.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public Optional<UserRepresentationalRequest> findByEmail(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserRepresentationalRequest[]> user = template.exchange(
                    base_url + "/auth/admin/realms/master/users?exact=true&email=" + email,
                    HttpMethod.GET, httpEntity, UserRepresentationalRequest[].class);
            if (Arrays.asList(user.getBody()).isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(Arrays.stream(user.getBody()).toList().get(0));
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public Optional<UserRepresentationalRequest> findByUsername(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserRepresentationalRequest[]> user = template.exchange(
                    base_url + "/auth/admin/realms/master/users?exact=true&username=" + username,
                    HttpMethod.GET, httpEntity, UserRepresentationalRequest[].class);
            if (Arrays.asList(user.getBody()).isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(Arrays.stream(user.getBody()).toList().get(0));
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }

    public RoleDtoResponse[] getRolesByUserId(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<RoleDtoResponse[]> roles = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + userId + "/role-mappings/realm",
                HttpMethod.GET, httpEntity, RoleDtoResponse[].class);

        return roles.getBody();
    }

    public Boolean addRoleByUserId(UUID user_id, RoleDtoResponse[] roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RoleDtoResponse[]> httpEntity = new HttpEntity<>(roles, headers);
        ResponseEntity<Void> res = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + user_id + "/role-mappings/realm",
                HttpMethod.POST, httpEntity, Void.class);

        return res.getStatusCode().is2xxSuccessful();
    }

    public Boolean removeRoleByUserId(UUID user_id, RoleDtoResponse[] roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RoleDtoResponse[]> httpEntity = new HttpEntity<>(roles, headers);
        ResponseEntity<Void> res = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + user_id + "/role-mappings/realm",
                HttpMethod.DELETE, httpEntity, Void.class);

        return res.getStatusCode().is2xxSuccessful();
    }

    public GroupDtoResponse[] getGroupsByUserId(UUID userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GroupDtoResponse[]> users = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + userId + "/groups",
                HttpMethod.GET, httpEntity, GroupDtoResponse[].class);

        return users.getBody();
    }

    public Boolean addGroupByUserId(UUID userId, UUID groupId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> res = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + userId + "/groups/" + groupId,
                HttpMethod.PUT, httpEntity, Void.class);

        return res.getStatusCode().is2xxSuccessful();
    }

    public Boolean removeGroupByUserId(UUID userId, UUID groupId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AdminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> res = template.exchange(
                base_url + "/auth/admin/realms/master/users/" + userId + "/groups/" + groupId,
                HttpMethod.DELETE, httpEntity, Void.class);

        return res.getStatusCode().is2xxSuccessful();
    }
}
