package org.example.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("emailNotifier")
@Order(1)
public class EmailNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("Email notification: " + message);
    }
}
