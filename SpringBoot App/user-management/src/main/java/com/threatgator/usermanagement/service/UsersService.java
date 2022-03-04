package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Users;
import org.springframework.boot.autoconfigure.security.SecurityProperties;


import java.util.List;

// Users Service Interface
public interface UsersService {
    public Users saveUser(Users user);
    public List<Users> getAllUsers();
}
