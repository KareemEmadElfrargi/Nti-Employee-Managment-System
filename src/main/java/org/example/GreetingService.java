package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class GreetingService {

    @PostConstruct
    public void init() {
        System.out.println("GreetingService initialized");
    }

    public String greet() {
        return "Hello from Spring Context!";
    }
}
