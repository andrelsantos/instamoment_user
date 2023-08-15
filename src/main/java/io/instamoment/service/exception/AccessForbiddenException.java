package io.instamoment.service.exception;

public class AccessForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccessForbiddenException(String message)
    {
        super(message);
    }
}
