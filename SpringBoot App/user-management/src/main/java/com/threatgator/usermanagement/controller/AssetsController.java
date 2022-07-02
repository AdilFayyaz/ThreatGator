package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.repository.AssetsRepository;
import com.threatgator.usermanagement.repository.OrganizationRepository;
import com.threatgator.usermanagement.service.AssetsService;
import com.threatgator.usermanagement.service.OrganizationService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Users Controller - Users functions (endpoints)
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private AssetsRepository assetsRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addAsset/{OrganizationId}")
    public ResponseEntity<Assets> createAsset(@PathVariable(value = "OrganizationId") Integer OrganizationId,
                                              @RequestBody Assets assets) {

        System.out.println(assets.getName());
        Assets assets1 = organizationService.getOrganization(OrganizationId).map(organization -> {
            assets.setOrganization(organization);
            return assetsService.saveAsset(assets);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Organization with id = " + OrganizationId));
        return new ResponseEntity<>(assets1, HttpStatus.CREATED);
    }
//delete an asset
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/deleteAsset/{OrganizationId}")
    public void deleteAsset(@PathVariable(value = "OrganizationId") Integer OrganizationId,
                            @RequestBody Assets assets) {

        System.out.println(assets.getName());

             assetsService.deleteAsset(assets);

    }

    // get all users
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getAll")
    public List<Assets> list(){
        return assetsService.getAllAssets();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/assetsByOrganization/{organizationId}")
    public ResponseEntity<List<Assets>> getAllAssetsByOrganizationId(@PathVariable(value = "organizationId") Integer OrganizationId) {
        if (!organizationRepository.existsById(OrganizationId)) {
            throw new ResourceNotFoundException("Not found Organization with id = " + OrganizationId);
        }

        List<Assets> assets = assetsRepository.findByOrganizationId(OrganizationId);
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }



}
