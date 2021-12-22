package com.threatgator.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.UsersService;

import java.util.List;

// Users Controller - Users functions (endpoints)
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // add a user
    @PostMapping("/adduser")
    public String add(@RequestBody Users user){
        usersService.saveUser(user);
        return "New user added";
    }
    // get all users
    @GetMapping("/getAll")
    public List<Users> list(){
        return usersService.getAllUsers();
    }

    // validate the credentials of the user
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
