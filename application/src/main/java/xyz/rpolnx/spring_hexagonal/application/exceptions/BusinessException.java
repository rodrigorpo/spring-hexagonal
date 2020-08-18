package xyz.rpolnx.spring_hexagonal.application.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
