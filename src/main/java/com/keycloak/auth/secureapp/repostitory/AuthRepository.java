package com.keycloak.auth.secureapp.repostitory;

import com.keycloak.auth.secureapp.model.ResponseToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class AuthRepository {
    private final RestTemplate template;

    public AuthRepository(RestTemplate template) {
        this.template = template;
    }

    @Value("${mykeycloak.admin-client-id}")
    private String admin_client_id;
    @Value("${mykeycloak.admin-client-secret}")
    private String admin_client_secret;
    @Value("${mykeycloak.admin-grant-type}")
    private String admin_grant_type;

    public String getAdminToken() {
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
}
