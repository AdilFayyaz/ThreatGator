package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.service.BookmarkService;
import com.threatgator.usermanagement.service.OrganizationService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.UsersService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Users Controller - Users functions (endpoints)
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private BookmarkService bookmarkService;

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
    public boolean validateCredentials(@RequestBody user_details userDetails){
        List<Users> usersList= usersService.getAllUsers();
        for (int i=0; i< usersList.size(); i++){
            if (usersList.get(i).getEmail().equals(userDetails.username) && usersList.get(i).getPassword().equals(userDetails.password)){
                return true;
            }
        }

        return false;
    }

    // toggleBookmark of report for user
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/toggleBookmark")
    public boolean toggleBookmark(@RequestBody String json_info) throws JSONException, IOException {
        JSONObject j = new JSONObject(json_info);
        return bookmarkService.toggleBookmark(j.getInt("userId"), j.getString("reportHash"));
    }

    // check if report bookmarked for user
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/checkBookmark")
    public boolean checkBookmark(@RequestBody String json_info) throws JSONException, IOException {
        JSONObject j = new JSONObject(json_info);
        return bookmarkService.isBookmarked(j.getInt("userId"), j.getString("reportHash"));
    }

    // get reports bookmarked for user
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getBookmarks/{userId}")
    public ArrayList<String> getBookmarks(@PathVariable Integer userId) throws JSONException, IOException {
        return bookmarkService.getBookmarks(userId);
    }
}
