package com.keycloak.auth.secureapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/groups")
public class GroupsController {
    @GetMapping
    public void getAllGroups() {
        // GET http://localhost:28080/auth/admin/realms/master/groups
    }

    @GetMapping("/users/role")
    public void getUsersByGroup() {
        // GET http://localhost:28080/auth/admin/realms/master/groups/{group_id}/members
    }

    @GetMapping(path = "/{id}")
    public void getGropusByUserId() {
        // GET http://localhost:28080/auth/admin/realms/master/users/{user_id}/groups
    }

    @PutMapping(path = "/{id}")
    public void addGroupByUserId() {
        // PUT http://localhost:28080/auth/admin/realms/master/users/{user_id}/groups/{group_id}
    }

    @DeleteMapping(path = "/{id}")
    public void removeGroupByUserId() {
        // DELETE http://localhost:28080/auth/admin/realms/master/users/{user_id}/groups/{group_id}
    }
}
