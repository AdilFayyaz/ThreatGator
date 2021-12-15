package com.threatgator.source_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.threatgator.source_management.model.Source;
import com.threatgator.source_management.service.SourcesService;

import java.util.List;
@RestController
@RequestMapping("/sources")
public class SourceController {
    @Autowired
    private SourcesService sourcesService;

    @PostMapping("/addSource")
    public String add(@RequestBody Source source){
        sourcesService.saveSource(source);
        return "New source added";
    }

    @GetMapping("/getAll")
    public List<Source> list(){
        return sourcesService.getAllSources();
    }
}

