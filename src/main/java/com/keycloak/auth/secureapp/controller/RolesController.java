package com.keycloak.auth.secureapp.controller;

import com.keycloak.auth.secureapp.model.RolesResponse;
import com.keycloak.auth.secureapp.model.UserRepresentational;
import com.keycloak.auth.secureapp.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/roles")
@AllArgsConstructor
public class RolesController {
    private final KeycloakService service;

    /*
    *  /admin/**  -> so podem ser acessados por usuario ADMIN
    *       /admin/roles -> gerenciamento de usuarios
    *       /admin/users -> gerenciamento de usuarios
    *       /admin/groups -> gerenciamente de grupos
    *  /auth/** -> publico qualque um pode acessar
    *       /auth/sigin -> login
    *       /auth/forgot_password -> esqueceu a senha
    */

    @GetMapping
    public void getAllRoles() {
        // GET http://localhost:28080/auth/admin/realms/master/roles

        /*
        podem ser mapeadas
        GET /{realm}/users/{id}/role-mappings/realm/available
         */
    }

    @GetMapping("{role_name}/users")
    public UserRepresentational[] getUsersByRole(@PathVariable String role_name) {
        return service.getUsersByRole(role_name);
    }

    @GetMapping(path = "users/{id}")
    public RolesResponse[] getRolesByUserId(@PathVariable UUID id) {
        return service.getRolesByUser(id);
    }

    @PutMapping(path = "users/{id}")
    public String addRoleByUserId(@PathVariable UUID id, @RequestBody RolesResponse[] rolesResponse) {
        //TODO: passar varias roles de uma vez ou uma de cada vez? a decidir
        return service.addRolByUserId(id, rolesResponse);
    }

    @DeleteMapping(path = "users/{id}")
    public void removeRolesByUserId() {
        // DELETE http://localhost:28080/auth/admin/realms/master/users/{user_id}/role-mappings/realm
    }
}
