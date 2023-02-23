package com.keycloak.auth.secureapp.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChooseVinculoDTO {
    private String refresh_token;
    private Long vinculo_id;
}
