package io.teamchallenge.controller;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for greeting.
 * @author Niktia Malov
 */
@RestController
public class HelloController {
    /**
     * Returns a greeting message.
     *
     * @param name An optional parameter representing the name of the person to greet.
     * @return A greeting message with the provided name, or "Hello, Unknown" if no name is provided.
     */
    @GetMapping("/hello")
    public String greetings(@RequestParam Optional<String> name, Authentication authentication) {
        return "Hello, " + name.orElse("Unknown") + authentication.getDetails();
    }
}