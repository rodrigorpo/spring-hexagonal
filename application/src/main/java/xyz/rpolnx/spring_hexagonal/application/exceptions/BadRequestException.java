package xyz.rpolnx.spring_hexagonal.application.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
