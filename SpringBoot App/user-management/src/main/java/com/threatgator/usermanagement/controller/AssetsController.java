package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.service.AssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.UsersService;

import java.util.List;

// Users Controller - Users functions (endpoints)
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private AssetsService assetsService;

    // add a user
    @PostMapping("/addAsset")
    public String add(@RequestBody Assets assets){
        assetsService.saveAsset(assets);
        return "New asset added";
    }
    // get all users
    @GetMapping("/getAll")
    public List<Assets> list(){
        return assetsService.getAllAssets();
    }

    // get all users
    @GetMapping("/getAdminAssets/{admin_id}")
    public List<Assets> getAssets(@PathVariable int admin_id){
        return assetsService.getUserAssets(admin_id);
    }


}
