package io.instamoment.service.exception;

public class PushNotificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PushNotificationException(String message) {
        super(message);
    }
}
