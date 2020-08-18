package xyz.rpolnx.spring_hexagonal.application.config;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionWrapper {
    private String cause;
    private HttpStatus status;
}
