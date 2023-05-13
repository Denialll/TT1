package net.naumen.springbackend.controller;

import ch.qos.logback.core.model.NamedModel;
import net.naumen.springbackend.service.NameService;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin("http://localhost:5173/")
public class NameController {

    private final NameService nameService;

    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    @PostMapping
    public int getUser(@RequestBody NamedModel username) {
        String name = username.getName();
        HashMap<String, Integer> userMap =  nameService.getNameMap();

        if(userMap.containsKey(name)) return userMap.get(name);

        int age = nameService.getAgeFromApi(name);
        nameService.addNameAge(name,age);

        return age;
    }
}