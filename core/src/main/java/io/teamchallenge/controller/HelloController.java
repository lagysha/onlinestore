package io.teamchallenge.controller;

import io.teamchallenge.annotation.CurrentUserId;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    /**
     * Returns a greeting message.
     *
     * @param name An optional parameter representinga the name of the person to greet.
     * @return A greeting message with the provided name, or "Hello, Unknown" if no name is provided.
     */
    @GetMapping("/hello")
    public String greetings(@RequestParam Optional<String> name, @CurrentUserId Long userId) {
        return "Hello, " + name.orElse("Unknown") + userId;
    }
}