package com.jwt.service;

import com.jwt.model.Role;
import com.jwt.model.User;

public interface UserService {

    User saveUser(User user);

    Role saveRole(Role role);

    void addUser(String username, String rolename);
}
