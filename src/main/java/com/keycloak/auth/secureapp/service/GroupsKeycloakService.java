package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.repostitory.GroupsKeycloakRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@AllArgsConstructor
public class GroupsKeycloakService {
    private final GroupsKeycloakRepository groupRepository;

    public GroupRepresentation[] getAllGroups() {
        return groupRepository.getAllGroups();
    }

    public UserRepresentationalResponse[] getUsersByGroup(UUID id) {
        return groupRepository.getUsersByGroup(id);
    }

    public GroupRepresentation[] getGroupsByUserId(UUID id) {
        return groupRepository.getGroupsByUserId(id);
    }
}
