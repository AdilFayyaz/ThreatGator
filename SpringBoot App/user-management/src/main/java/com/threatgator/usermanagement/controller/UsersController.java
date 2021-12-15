package com.threatgator.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.UsersService;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/adduser")
    public String add(@RequestBody Users user){
        usersService.saveUser(user);
        return "New user added";
    }
    @GetMapping("/getAll")
    public List<Users> list(){
        return usersService.getAllUsers();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/validateCredentials")
    public boolean validateCredentials(@RequestBody user_details userDetails){
        List<Users> usersList= usersService.getAllUsers();
        for (int i=0; i< usersList.size(); i++){
            if (usersList.get(i).getEmail().equals(userDetails.username) && usersList.get(i).getPassword().equals(userDetails.password)){
                return true;
            }
        }

        return false;
    }
}
