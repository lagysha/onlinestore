package io.teamchallenge.onlinestore.controllers;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String greetings(@RequestParam Optional<String> name){
        return "Hello, " + name.orElse("Unknown");
    }
}
