package org.example.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("smsNotifier")
@Order(2)
public class SmsNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("SMS notification: " + message);
    }
}
