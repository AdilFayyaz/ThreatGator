package com.threatgator.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threatgator.usermanagement.model.Admin;
import com.threatgator.usermanagement.repository.AdminRepository;

import java.util.List;

// Implm of Admin Service
@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminRepository AdminRepository;

    // Save user and get all admin functions
    @Override
    public Admin saveUser(Admin user) {
        return AdminRepository.save(user);
    }
    @Override
    public List<Admin> getAllAdmin(){
        return AdminRepository.findAll();
    }
}
