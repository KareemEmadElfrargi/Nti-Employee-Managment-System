package org.example.audit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope("prototype")
public class AuditLogger {

    public void log(String message) {
        System.out.println("[" + LocalDateTime.now() + "] (" + this + ") " + message);
    }
}
