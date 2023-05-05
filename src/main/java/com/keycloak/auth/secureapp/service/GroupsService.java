package com.keycloak.auth.secureapp.service;

import com.keycloak.auth.secureapp.dto.GroupDtoResponse;
import com.keycloak.auth.secureapp.dto.UserRepresentationalResponse;
import com.keycloak.auth.secureapp.repostitory.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupsService {
    private final GroupsRepository repository;

    public GroupDtoResponse[] getAllGroups() {
        return repository.getAllGroups();
    }

    public Optional<UserRepresentationalResponse[]> getUsersByGroupId(UUID id) {
        Optional<GroupDtoResponse> group = repository.findById(id);

        if (group.isPresent()) {
            UserRepresentationalResponse[] users = repository.getUsersByGroup(id);
            return Optional.ofNullable(users);
        }
        return Optional.empty();
    }

    public Optional<GroupDtoResponse> findById(UUID id) {
        return repository.findById(id);
    }
}
