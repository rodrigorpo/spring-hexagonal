package xyz.rpolnx.spring_hexagonal.application.exceptions;

public class UnexpectedErrorException extends RuntimeException {
    public UnexpectedErrorException(String message) {
        super(message);
    }
}
