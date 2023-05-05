package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.GroupDtoResponse;
import com.keycloak.auth.secureapp.dto.UserRepresentationalResponse;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mykeycloak.base-url}")
    private String base_url;

    public GroupDtoResponse[] getAllGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GroupDtoResponse[]> groups = template.exchange(
                base_url + "/auth/admin/realms/master/groups",
                HttpMethod.GET, httpEntity, GroupDtoResponse[].class);

        return groups.getBody();
    }

    public UserRepresentationalResponse[] getUsersByGroup(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRepresentationalResponse[]> users = template.exchange(
                base_url + "/auth/admin/realms/master/groups/" + id + "/members",
                HttpMethod.GET, httpEntity, UserRepresentationalResponse[].class);

        return users.getBody();
    }

    public Optional<GroupDtoResponse> findById(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminRepository.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GroupDtoResponse> group = template.exchange(
                    base_url + "/auth/admin/realms/master/groups/" + id,
                    HttpMethod.GET, httpEntity, GroupDtoResponse.class);
            return Optional.ofNullable(group.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
