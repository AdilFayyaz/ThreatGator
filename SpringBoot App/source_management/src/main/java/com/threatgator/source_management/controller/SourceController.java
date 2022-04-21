package com.threatgator.source_management.controller;

import com.threatgator.source_management.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.source_management.model.Source;
import com.threatgator.source_management.service.SourcesService;
import com.threatgator.source_management.service.AccountsService;
import java.util.List;

// Class containing end points to source management (Controller class)
@RestController
@RequestMapping("/sources")
public class SourceController {
    @Autowired // Allow object to inject the object dependency implicitly
    private SourcesService sourcesService;
    private AccountsService accountsService;

    public SourceController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    // Add Source Controller
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addSource")
    public String add(@RequestBody Source source){
        sourcesService.saveSource(source);
        return "New source added";
    }

    // Add Account controller
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addSource/account")
    public String addAccounts(@RequestBody Account account){
        accountsService.saveAccount(account);
        return "New account added";
    }

    // Get list of all accounts
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getAll/listAccount")
    public List<Account> listAccounts(){
        return accountsService.getAllAccounts();
    }

    // delete an account
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/delete/account/{id}")
    public String deleteAccount(@PathVariable int id){
        return accountsService.deleteAccount(id);
    }

    // delete a source
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/delete/source/{id}")
    public String deleteSource(@PathVariable int id){
        return sourcesService.deleteSource(id);
    }

    // get all sources data
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getAll")
    public List<Source> list(){
        return sourcesService.getAllSources();
    }
}

