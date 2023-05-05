package com.keycloak.auth.secureapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class TokenDecoder {
    public String preferred_username;
    public String[] groups;
    //public String[] roles;
    public Long vinculo_id;

    public static TokenDecoder getDecoded(String encodedToken) {
        String[] pieces = encodedToken.split("\\.");
        String b64payload = pieces[1];
        String stringToParse = new String(Base64.decodeBase64(b64payload), StandardCharsets.UTF_8);

        return new Gson().fromJson(stringToParse, TokenDecoder.class);
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
