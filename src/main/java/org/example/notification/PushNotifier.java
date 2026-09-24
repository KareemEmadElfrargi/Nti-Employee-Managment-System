package org.example.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("pushNotifier")
@Order(3)
public class PushNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("Push notification: " + message);
    }
}
