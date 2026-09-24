package org.example.notification;

import org.springframework.stereotype.Component;

@Component("pushNotifier")
public class PushNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("Push notification: " + message);
    }
}
