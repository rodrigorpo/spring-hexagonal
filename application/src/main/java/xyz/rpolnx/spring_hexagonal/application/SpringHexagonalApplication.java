package xyz.rpolnx.spring_hexagonal.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan("xyz.rpolnx.spring_hexagonal")
@EnableJpaRepositories("xyz.rpolnx.spring_hexagonal.infrastructure.repository")
@EntityScan("xyz.rpolnx.spring_hexagonal.infrastructure.entity")
public class SpringHexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHexagonalApplication.class, args);
    }

}