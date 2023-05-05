package com.keycloak.auth.secureapp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupDtoResponse {
    private UUID id;
    private String name;

    //TODO: inserir atributo descriçao
}
