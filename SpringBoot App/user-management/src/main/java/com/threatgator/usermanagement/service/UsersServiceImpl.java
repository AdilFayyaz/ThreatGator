package com.threatgator.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.repository.UsersRepository;

import java.util.List;
// Users service impl
@Service
public class UsersServiceImpl implements UsersService{
    @Autowired
    private UsersRepository usersRepository;

    // save and get all users
    @Override
    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }
    @Override
    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }
}
