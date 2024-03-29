package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.SetVinculoDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoRequest;
import com.keycloak.auth.secureapp.dto.LogoutDtoResponse;
import com.keycloak.auth.secureapp.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class AuthRepository {
    private final RestTemplate template;
    private final AdminRepository adminRepository;

    public AuthRepository(RestTemplate template, AdminRepository adminRepository) {
        this.template = template;
        this.adminRepository = adminRepository;
    }

    @Value("${mykeycloak.client-id}")
    private String client_id;
    @Value("${mykeycloak.client-secret}")
    private String client_secret;
    @Value("${mykeycloak.base-url}")
    private String base_url;

    public ResponseEntity<LogoutDtoResponse> logout(LogoutDtoRequest logoutRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(adminRepository.getAdminToken());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", client_id);
        map.add("client_secret", client_secret);
        map.add("refresh_token", logoutRequest.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);

        ResponseEntity<LogoutDtoResponse> response = template.postForEntity(
                base_url + "/auth/realms/master/protocol/openid-connect/logout",
                httpEntity, LogoutDtoResponse.class);

        LogoutDtoResponse res = new LogoutDtoResponse();
        if(response.getStatusCode().is2xxSuccessful()) {
            res.setMessage("Logged out successfully");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public Optional<TokenResponse> setVinculo(SetVinculoDtoRequest chooseVinculoDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(adminRepository.getAdminToken());

        MultiValueMap<String, String> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("client_id", client_id);
        userCredentials.add("client_secret", client_secret);
        userCredentials.add("grant_type", "refresh_token");
        userCredentials.add("refresh_token", chooseVinculoDTO.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userCredentials, headers);

        try {
            ResponseEntity<TokenResponse> res = template.postForEntity(
                    base_url + "/auth/realms/master/protocol/openid-connect/token",
                    httpEntity, TokenResponse.class);
            return Optional.ofNullable(res.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
