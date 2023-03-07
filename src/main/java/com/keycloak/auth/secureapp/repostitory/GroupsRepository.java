package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Component
public class GroupsRepository {
    private final RestTemplate template;
    private final AdminRepository adminRepository;

    public GroupsRepository(RestTemplate template, AdminRepository adminRepository) {
        this.template = template;
        this.adminRepository = adminRepository;
    }

    // ou eu retorno um vetor preenchido ou vazio
    public GroupRepresentation[] getAllGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GroupRepresentation[]> groups = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/groups",
                HttpMethod.GET, httpEntity, GroupRepresentation[].class);

        return groups.getBody();
    }

    // se o id existir retorna vetor preenchido staus 200 OK
    // se o id nao existir retorna vetor nulo status 404 not found
    public UserRepresentationalResponse[] getUsersByGroup(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                "http://localhost:28080/auth/admin/realms/master/groups/" + id + "/members",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    // se o id existir retorna a group status 200 OK
    // se o id nao existir retorna vazio status 404 not found
    public Optional<GroupRepresentation> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GroupRepresentation> group = template.exchange(
                    "http://localhost:28080/auth/admin/realms/master/groups/"+id,
                    HttpMethod.GET, httpEntity, GroupRepresentation.class);
            return Optional.ofNullable(group.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
