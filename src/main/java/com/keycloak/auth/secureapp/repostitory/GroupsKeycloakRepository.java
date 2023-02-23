package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@Component
public class GroupsKeycloakRepository {
    private final RestTemplate template;
    private final AuthRepository authRepository;

    public GroupsKeycloakRepository(RestTemplate template, AuthRepository authRepository) {
        this.template = template;
        this.authRepository = authRepository;
    }

    public GroupRepresentation[] getAllGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GroupRepresentation[]> groups = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/groups",
                HttpMethod.GET, httpEntity, GroupRepresentation[].class);

        return groups.getBody();
    }

    public UserRepresentationalResponse[] getUsersByGroup(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/groups/"+id+"/members",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public GroupRepresentation[] getGroupsByUserId(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GroupRepresentation[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/users/"+id+"/groups",
                HttpMethod.GET, httpEntity, GroupRepresentation[].class);

        return users.getBody();
    }
}
