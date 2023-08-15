package io.instamoment.service.exception;

public class ExistingLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistingLoginException(String mensagem) {
        super(mensagem);
    }

}
