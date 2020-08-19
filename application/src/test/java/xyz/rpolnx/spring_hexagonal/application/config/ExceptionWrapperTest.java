package xyz.rpolnx.spring_hexagonal.application.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ExceptionWrapperTest {

    @Test
    public void dumb() {
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper("", HttpStatus.BAD_GATEWAY);

        Assertions.assertEquals("", exceptionWrapper.getCause());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, exceptionWrapper.getStatus());
    }
}