package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.model.GroupRepresentation;
import com.keycloak.auth.secureapp.model.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.repostitory.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupsService {
    private final GroupsRepository repository;

    public GroupRepresentation[] getAllGroups() {
        return repository.getAllGroups();
    }

    public Optional<UserRepresentationalResponse[]> getUsersByGroupId(UUID id) {
        Optional<GroupRepresentation> group = repository.findById(id);

        if (group.isPresent()) {
            UserRepresentationalResponse[] users = repository.getUsersByGroup(id);
            return Optional.ofNullable(users);
        }
        return Optional.empty();
    }

    public Optional<GroupRepresentation> findById(UUID id) {
        return repository.findById(id);
    }
}
