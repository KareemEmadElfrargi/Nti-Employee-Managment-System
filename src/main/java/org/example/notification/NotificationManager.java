package org.example.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NotificationManager {

    private final List<Notifier> notifiers;

    @Value("${notification.retry-count}")
    private int retryCount;

    public NotificationManager(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyAll(String message) {
        for (Notifier notifier : notifiers) {
            sendWithRetry(notifier, message);
        }
    }

    private void sendWithRetry(Notifier notifier, String message) {
        RuntimeException lastFailure = null;
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                notifier.send(message);
                return;
            } catch (RuntimeException e) {
                lastFailure = e;
                System.out.println("Notification attempt " + attempt + " failed: " + e.getMessage());
            }
        }
        if (lastFailure != null) {
            System.out.println("Giving up after " + retryCount + " attempts: " + lastFailure.getMessage());
        }
    }
}
