package com.threatgator.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    // add user to the table
    @PostMapping("/adduser")
    public String add(@RequestBody Admin user){
        AdminService.saveUser(user);
        return "New user added";
    }
    // get all users
    @GetMapping("/getAll")
    public List<Admin> list(){
        return AdminService.getAllAdmin();
    }

    // validate the admins credentials
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/validateAdminCredentials")
    public boolean validateCredentials(@RequestBody user_details userDetails){
        List<Admin> AdminList= AdminService.getAllAdmin();
        for (int i=0; i< AdminList.size(); i++){
            if (AdminList.get(i).getEmail().equals(userDetails.username) && AdminList.get(i).getPassword().equals(userDetails.password)){
                return true;
            }
        }

        return false;
    }
}
