package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Organization;
import com.threatgator.usermanagement.service.OrganizationService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private OrganizationService organizationService;

    // add a user
    @PostMapping("/adduser")
    public String add(@RequestBody Users user){
        usersService.saveUser(user);
        return "New user added";
    }

    @PostMapping("/addUser/{OrganizationId}")
    public ResponseEntity<Users> createUser(@PathVariable(value = "OrganizationId") Integer OrganizationId,
                                              @RequestBody Users users) {

        System.out.println(users.getName());
        Users users1 = organizationService.getOrganization(OrganizationId).map(organization -> {
            users.setOrganization(organization);
            return usersService.saveUser(users);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Organization with id = " + OrganizationId));
        return new ResponseEntity<>(users1, HttpStatus.CREATED);
    }

    // get all users
    @GetMapping("/getAll")
    public List<Users> list(){
        return usersService.getAllUsers();
    }

    // validate the credentials of the user
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/validateCredentials")
    public Organization validateCredentials(@RequestBody user_details userDetails){
Organization organization = new Organization();
        List<Users> usersList= usersService.getAllUsers();
//        System.out.println(usersList);
        for (int i=0; i< usersList.size(); i++){
//            System.out.println("abc "+usersList.get(i));
            if (usersList.get(i).getEmail().equals(userDetails.username) && usersList.get(i).getPassword().equals(userDetails.password)){
//                System.out.println("found");
                return usersList.get(i).getOrganization();
            }
        }

        return organization ;
    }
}
