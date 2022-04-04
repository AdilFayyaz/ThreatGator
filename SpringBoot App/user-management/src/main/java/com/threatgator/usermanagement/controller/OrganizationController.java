package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.model.Organization;
import com.threatgator.usermanagement.service.AssetsService;
import com.threatgator.usermanagement.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.service.UsersService;

import java.util.List;

// Users Controller - Users functions (endpoints)
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    // add organization
    @PostMapping("/addOrganization")
    public String add(@RequestBody Organization organization){
        organizationService.saveOrganization(organization);
        return "New organization added";
    }

    @GetMapping("/getAll")
    public List<Organization> getAll(){
        return organizationService.getAll();
    }

    @GetMapping("/getName/{Org_id}")
    public String getName(@PathVariable(value = "Org_id") Integer org_id){
        return organizationService.getName(org_id);
    }

    @GetMapping("/getCountry/{Org_id}")
    public String getCountry(@PathVariable(value = "Org_id") Integer org_id){
        return organizationService.getCountry(org_id);
    }

    @GetMapping("/getSector/{Org_id}")
    public String getSector(@PathVariable(value = "Org_id") Integer org_id){
        return organizationService.getSector(org_id);
    }
}