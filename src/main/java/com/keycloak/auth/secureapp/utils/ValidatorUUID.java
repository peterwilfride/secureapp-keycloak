package com.keycloak.auth.secureapp.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class ValidatorUUID {

    public Boolean validate(UUID id) {
        Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

        return !UUID_REGEX.matcher(id.toString()).matches();
    }
}
