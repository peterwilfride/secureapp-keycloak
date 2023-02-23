package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.service.GroupsKeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/groups")
@AllArgsConstructor
public class GroupsController {
    public final GroupsKeycloakService service;

    @GetMapping
    public GroupRepresentation[] getAllGroups() {
        return service.getAllGroups();
    }

    @GetMapping("{id}/users")
    public UserRepresentationalResponse[] getUsersByGroup(@PathVariable UUID id) {
        return service.getUsersByGroup(id);
    }

    @GetMapping(path = "/users/{id}")
    public GroupRepresentation[] getGropsByUserId(@PathVariable UUID id) {
        return service.getGroupsByUserId(id);
    }
}
