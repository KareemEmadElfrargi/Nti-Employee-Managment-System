package org.example;

import org.example.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService greetingService = context.getBean(GreetingService.class);
            System.out.println(greetingService.greet());
        }
    }
}
