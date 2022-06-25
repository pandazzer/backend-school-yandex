package com.example.backendschoolyandex.Controller;

import com.example.backendschoolyandex.Services.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping
public class Controller {

    @Autowired
    private Service service;

    @PostMapping(path = "/imports")
    public ResponseEntity imports(@RequestBody String json) throws JsonProcessingException, ParseException {
        return service.imports(json);
    }

    @DeleteMapping(path = "/delete/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable("id") String id) throws JsonProcessingException {
        return service.delete(id);
    }

    @GetMapping(path = "/nodes/{id}")
    public ResponseEntity nodes(@PathVariable("id") String id) throws JsonProcessingException {
        return service.nodes(id);
    }
}