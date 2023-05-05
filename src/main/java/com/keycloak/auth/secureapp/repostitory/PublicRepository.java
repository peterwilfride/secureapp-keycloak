package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.dto.LoginDtoRequest;
import com.keycloak.auth.secureapp.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
@Component
public class PublicRepository {
    private final RestTemplate template;

    public PublicRepository(RestTemplate template) {
        this.template = template;
    }

    @Value("${mykeycloak.client-id}")
    private String client_id;
    @Value("${mykeycloak.client-secret}")
    private String client_secret;
    @Value("${mykeycloak.grant-type}")
    private String grant_type;
    @Value("${mykeycloak.base-url}")
    private String base_url;


    public Optional<TokenResponse> authenticate(LoginDtoRequest loginDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> userCredentials = new LinkedMultiValueMap<>();
        userCredentials.add("client_id", client_id);
        userCredentials.add("client_secret", client_secret);
        userCredentials.add("grant_type", grant_type);
        userCredentials.add("username", loginDTO.getUsername());
        userCredentials.add("password", loginDTO.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userCredentials, headers);

        try {
            ResponseEntity<TokenResponse> res = template.postForEntity(
                    base_url + "/realms/master/protocol/openid-connect/token",
                    httpEntity, TokenResponse.class);
            return Optional.ofNullable(res.getBody());
        } catch (final HttpClientErrorException e) {
            return Optional.empty();
        }
    }
}
