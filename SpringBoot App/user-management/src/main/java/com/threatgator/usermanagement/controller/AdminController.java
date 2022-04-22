package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Organization;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.OrganizationService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Admin;
import com.threatgator.usermanagement.service.AdminService;

import java.util.List;

// Admin controller class
@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService AdminService;
    @Autowired
    private OrganizationService organizationService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    // add user to the table
    @PostMapping("/adduser")
    public String add(@RequestBody Admin user){
        AdminService.saveUser(user);
        return "New admin added";
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addAdmin/{OrganizationId}")
    public ResponseEntity<Admin> createAdmin(@PathVariable(value = "OrganizationId") Integer OrganizationId,
                                            @RequestBody Admin admin) {

        System.out.println(admin.getName());
        Admin admin1 = organizationService.getOrganization(OrganizationId).map(organization -> {
            admin.setOrganization(organization);
            return AdminService.saveUser(admin);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Organization with id = " + OrganizationId));
        return new ResponseEntity<>(admin1, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    // get all users
    @GetMapping("/getAll")
    public List<Admin> list(){
        return AdminService.getAllAdmin();
    }

    // validate the admins credentials
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/validateAdminCredentials")
    public Organization validateCredentials(@RequestBody user_details userDetails){
        Organization organization = new Organization();
        List<Admin> usersList= AdminService.getAllAdmin();
        for (int i=0; i< usersList.size(); i++){
            if (usersList.get(i).getEmail().equals(userDetails.username) && usersList.get(i).getPassword().equals(userDetails.password)){
                return usersList.get(i).getOrganization();
            }
        }

        return organization ;
    }
    //return userid
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getAdminId")
    public int getAdminId(@RequestBody user_details userDetails){

        List<Admin> usersList= AdminService.getAllAdmin();;
        for (int i=0; i< usersList.size(); i++){
            if (usersList.get(i).getEmail().equals(userDetails.username) && usersList.get(i).getPassword().equals(userDetails.password)){
                return usersList.get(i).getId();
            }
        }

        return 0 ;
    }
}
