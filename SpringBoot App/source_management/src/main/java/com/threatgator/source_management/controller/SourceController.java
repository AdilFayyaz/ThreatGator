package com.threatgator.source_management.controller;

import com.threatgator.source_management.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.source_management.model.Source;
import com.threatgator.source_management.service.SourcesService;
import com.threatgator.source_management.service.AccountsService;
import java.util.List;
@RestController
@RequestMapping("/sources")
public class SourceController {
    @Autowired
    private SourcesService sourcesService;
    private AccountsService accountsService;

    public SourceController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addSource")
    public String add(@RequestBody Source source){
        sourcesService.saveSource(source);
        return "New source added";
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/addSource/account")
    public String addAccounts(@RequestBody Account account){
        accountsService.saveAccount(account);
        return "New account added";
    }
    @GetMapping("/getAll/listAccount")
    public List<Account> listAccounts(){
        return accountsService.getAllAccounts();
    }
    @GetMapping("/delete/account/{id}")
    public String deleteAccount(@PathVariable int id){
        return accountsService.deleteAccount(id);
    }
    @GetMapping("/delete/source/{id}")
    public String deleteSource(@PathVariable int id){
        return sourcesService.deleteSource(id);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getAll")
    public List<Source> list(){
        return sourcesService.getAllSources();
    }
}

