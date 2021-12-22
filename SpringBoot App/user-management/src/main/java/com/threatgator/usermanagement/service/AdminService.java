package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Admin;
import org.springframework.boot.autoconfigure.security.SecurityProperties;


import java.util.List;

// Admin Service Interface
public interface AdminService {
    public Admin saveUser(Admin user);
    public List<Admin> getAllAdmin();
}
