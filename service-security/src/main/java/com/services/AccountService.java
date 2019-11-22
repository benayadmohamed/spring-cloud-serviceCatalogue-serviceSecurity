package com.services;

import com.entities.Roles;
import com.entities.Users;

public interface AccountService {

    Users addUser(String username, String password, String confirmPassword);

    Roles addRole(Roles role);

    Users addRoleToUser(String username, String role);

    void deleteALL();

    Users loadUserbyUsername(String username);


}
